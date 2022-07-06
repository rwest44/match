package com.ck.usercenter.service.impl;

import com.ck.usercenter.mapper.UserMapper;
import com.ck.usercenter.model.domain.User;
import com.ck.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest implements Serializable {

    private static final long serialVersionUID = -5275579861747261458L;
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @Test
    void searchUsersByTags () {
        User user0 = new User();
        user0.setUsername("0000");

        user0.setUserPassword("11111111");
        user0.setTags("[\"java\",\"python\", \"C++\", \"dayi\"]");
        User user1 = new User();
        user1.setUsername("0001");

        user1.setUserPassword("11111111");
        user1.setTags("[\"java\",\"male\", \"C++\", \"dayi\"]");
        User user2 = new User();
        user2.setUsername("0002");

        user2.setUserPassword("11111111");
        user2.setTags("[\"python\",\"female\", \"frontend\", \"working\"]");
        User user3 = new User();
        user3.setUsername("0003");

        user3.setUserPassword("11111111");
        user3.setTags("[\"java\",\"mail\", \"algorithm\", \"working\"]");
        User user4 = new User();
        user4.setUsername("0004");

        user4.setUserPassword("11111111");
        user4.setTags("[\"java\",\"mail\", \"dasan\"]");
        User user5 = new User();
        user5.setUsername("0005");

        user5.setUserPassword("11111111");
        user5.setTags("[\"c++\",\"femail\", \"algorithm\", \"working\"]");
        User user6 = new User();
        user6.setUsername("0006");

        user6.setUserPassword("11111111");
        user6.setTags("[\"java\",\"mail\", \"dasan\"]");
        User user7 = new User();
        user7.setUsername("0007");

        user7.setUserPassword("11111111");
        user7.setTags("[\"python\",\"mail\", \"daer\"]");
        User user8 = new User();
        user8.setUsername("0008");

        user8.setUserPassword("11111111");
        user8.setTags("[\"c++\",\"mail\", \"working\"]");
        User user9 = new User();
        user9.setUsername("0009");

        user9.setUserPassword("11111111");
        user9.setTags("[\"java\",\"mail\", \"working\"]");

        Random random = new Random();
        Random random1 = new Random();
        for (int i = 0; i < 499; i++) {

            int number = random.nextInt(10);
            int number1 = random1.nextInt(50000);
            switch (number) {
                case 0:
                    user0.setUserAccount("0000" + number1);
                    userMapper.insert(user0);
                    break;
                case 1:
                    user1.setUserAccount("0001" + number1);
                    userMapper.insert(user1);
                    break;
                case 2:
                    user2.setUserAccount("0002" + number1);
                    userMapper.insert(user2);
                    break;
                case 3:
                    user3.setUserAccount("0003" + number1);
                    userMapper.insert(user3);
                    break;
                case 4:
                    user4.setUserAccount("0004" + number1);
                    userMapper.insert(user4);
                    break;
                case 5:
                    user5.setUserAccount("0005" + number1);
                    userMapper.insert(user5);
                    break;
                case 6:
                    user6.setUserAccount("0006" + number1);
                    userMapper.insert(user6);
                    break;
                case 7:
                    user7.setUserAccount("0007" + number1);
                    userMapper.insert(user7);
                    break;
                case 8:
                    user8.setUserAccount("0008" + number1);
                    userMapper.insert(user8);
                    break;
                case 9:
                    user9.setUserAccount("0009" + number1);
                    userMapper.insert(user9);
                    break;

            }


        }
    }
    @Test
    void searchUsers(){
        HashMap hashMap = new HashMap(27);
        List<String> tagNameList = Arrays.asList("java","dasan","mail");
        List<User> userList = userService.searchUsersByTags(tagNameList);
    }
}