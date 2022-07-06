package com.ck.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.usercenter.common.ErrorCode;
import com.ck.usercenter.exception.BusinessException;
import com.ck.usercenter.model.domain.User;
import com.ck.usercenter.service.UserService;
import com.ck.usercenter.mapper.UserMapper;
import com.ck.usercenter.utils.LazySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ck.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author ck
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "ckck";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {

        //校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");
        }
        //账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号中包含特殊字符");
        }

        //密码与校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码与校验密码不同");
        }

        //账号不能重复
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userAccount", userAccount);
//        long count = userMapper.selectCount(queryWrapper);
        if (ifRepeat("userAccount", userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }


        //星球编号不能重复
//        queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("planetCode", planetCode);
//        count = userMapper.selectCount(queryWrapper);
        if (ifRepeat("planetCode", planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号已存在");
        }

        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.FAIL, "插入数据失败");
        }

        return user.getId();
    }


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入的账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入的密码过短");
        }

        //账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号中包含特殊字符");
        }

        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        //用户不存在
        if (loginUser(userAccount, encryptPassword) == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号密码错误");
        }

        //用户脱敏
        User safetyUser = getSafetyUser(loginUser(userAccount, encryptPassword));
        //记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        LazySingleton lazySingleton = LazySingleton.getInstance();
        lazySingleton.addCount();
        log.info("用户：" + userAccount + " 登录成功，" + "当前网站共有：" + LazySingleton.getInstance().getCount() + " 个用户登录");

        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "originUser为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }


    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        LazySingleton lazySingleton = LazySingleton.getInstance();
        lazySingleton.reduceCount();
        System.out.println("用户注销，当前网站共有：" + LazySingleton.getInstance().getCount() + " 个用户登录");
        request.getSession().removeAttribute(USER_LOGIN_STATE);


        return 1;
    }


    /**
     * SQL语句查询法
     * 拼接 And 查询
     *
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {

        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName : tagNameList) {
            //遍历tagNameList，将每个标签都拼接上去，链式操作符，queryWrapper.like().like()实现比如 like '%Java%' and like '%Python'
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

    }


    /**
     * 判断是否重复
     *
     * @param column
     * @param val
     * @return
     */
    @Override
    public boolean ifRepeat(String column, String val) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, val);
        long count = userMapper.selectCount(queryWrapper);
        return count != 0;
    }

    /**
     * 返回登录信息
     *
     * @param userAccount
     * @param encryptPassword
     * @return
     */
    @Override
    public User loginUser(String userAccount, String encryptPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        return userMapper.selectOne(queryWrapper);

    }


    @Override
    public List<User> recommendUsers(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        String jsonUserTag = user.getTags();

        //判空
        if (StringUtils.isEmpty(jsonUserTag)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        //将json格式用户标签转换为String
        Gson gson = new Gson();
        List<String> listUserTags = gson.fromJson(jsonUserTag, new TypeToken<List<String>>() {}.getType());
        String userTags = String.join(",", listUserTags);

        //1.先查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);

        //声明一个空的推荐用户列表
        List<User> recommendUsers = new ArrayList<>();

        //2.遍历所有用户，当相似度大于一定值时成为推荐的伙伴
        for (User tempUser : userList) {
            String jsonTags = tempUser.getTags();
            if (StringUtils.isBlank(jsonTags)) {
                break;
            }
            List<String> listTags = gson.fromJson(jsonTags, new TypeToken<List<String>>() {}.getType());
            String recommendTags = String.join(",", listTags);

            tempUser.setSimilarDegree(getSimilarDegree(userTags, recommendTags));
            if (tempUser.getSimilarDegree() >= 0.6) {
                recommendUsers.add(tempUser);
            }
        }
        // 相似度高的先展示
        recommendUsers = recommendUsers.stream().sorted(Comparator.comparing(User::getSimilarDegree).reversed())
                .collect(Collectors.toList());

        return recommendUsers;


    }


    /**
     * 余弦相似度计算算法
     * @param userTags
     * @param recommendTags
     * @return
     */
        public Double getSimilarDegree (String userTags, String recommendTags){

            //创建向量空间模型，使用map实现，主键为词项，值为长度为2的数组，存放着对应词项在字符串中的出现次数
            Map<String, int[]> vectorSpace = new HashMap<>();

            //为了避免频繁产生局部变量，所以将itemCountArray声明在此
            int[] itemCountArray = null;

            //以空格为分隔符，分解字符串
            String[] strArray = userTags.split(",");
            for (int i = 0; i < strArray.length; ++i) {
                if (vectorSpace.containsKey(strArray[i]))
                    ++(vectorSpace.get(strArray[i])[0]);
                else {
                    itemCountArray = new int[2];
                    itemCountArray[0] = 1;
                    itemCountArray[1] = 0;
                    vectorSpace.put(strArray[i], itemCountArray);
                }
            }
            strArray = recommendTags.split(",");
            for (int i = 0; i < strArray.length; ++i) {
                if (vectorSpace.containsKey(strArray[i]))
                    ++(vectorSpace.get(strArray[i])[1]);
                else {
                    itemCountArray = new int[2];
                    itemCountArray[0] = 0;
                    itemCountArray[1] = 1;
                    vectorSpace.put(strArray[i], itemCountArray);
                }
            }
            //计算相似度
            double vector1Modulo = 0.00;//向量1的模
            double vector2Modulo = 0.00;//向量2的模
            double vectorProduct = 0.00;//向量积
            Iterator iter = vectorSpace.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                itemCountArray = (int[]) entry.getValue();
                vector1Modulo += itemCountArray[0] * itemCountArray[0];
                vector2Modulo += itemCountArray[1] * itemCountArray[1];
                vectorProduct += itemCountArray[0] * itemCountArray[1];
            }
            vector1Modulo = Math.sqrt(vector1Modulo);
            vector2Modulo = Math.sqrt(vector2Modulo);
            //返回相似度
            return (vectorProduct / (vector1Modulo * vector2Modulo));
        }


    /**
     * 根据标签搜索用户(内存过滤)
     *
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    @Deprecated
    private List<User> searchUsersByTagsBySQL(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //1.先查询所有用户
        Gson gson = new Gson();
        List<User> userList = userMapper.selectList(queryWrapper);
        //2.从内存中判断每个用户是否包含要查询的标签
        List<User> userList1 = userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            //用ofNullable()封装一个可能为空的对象，如果为空，则取orElse()中的值
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
        return userList1;
    }

}




