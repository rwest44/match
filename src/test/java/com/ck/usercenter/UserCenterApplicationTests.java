package com.ck.usercenter;

import com.ck.usercenter.mapper.UserMapper;
import com.ck.usercenter.model.domain.User;
import com.ck.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;




    }



