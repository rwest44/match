//package com.ck.usercenter;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.ck.usercenter.mapper.UserMapper;
//import com.ck.usercenter.model.domain.User;
//import com.ck.usercenter.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.StopWatch;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@SpringBootTest
//@Slf4j
//class UserCenterApplicationTests {
//
//    @Resource
//    private UserService userService;
//    @Resource
//    private UserMapper userMapper;
//
//    @Test
//    public void testQuery(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        userMapper.selectCount(null);
//        List<String> list = Arrays.asList("java", "mail", "working");
//
//        Long startTime1 = System.currentTimeMillis();
//        List<User> userList1 = userService.searchUsersByTagsByMemory(list);
//        System.out.println(userList1);
//        log.info("内存查询：" + (System.currentTimeMillis() - startTime1));
//
//        Long startTime = System.currentTimeMillis();
//        List<User> userList = userService.searchUsersByTags(list);
//        System.out.println(userList);
//        log.info("SQL查询：" + (System.currentTimeMillis() - startTime));
//
//
//
//    }
//
//
//
//    }
//
//
//
