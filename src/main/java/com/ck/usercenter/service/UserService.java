package com.ck.usercenter.service;

import com.ck.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author ck
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     *
     * @return 新用户id
     */

    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     *
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 判断是否重复
     * @param column
     * @param val
     * @return
     */
    boolean ifRepeat(String column, String val);


    /**
     * 返回登录信息
     * @param userAccount
     * @param encryptPassword
     * @return
     */
    User loginUser(String userAccount, String encryptPassword);

    /**
     * 智能推荐伙伴
     * @param request
     * @return
     */
    List<User> recommendUsers(HttpServletRequest request);
}
