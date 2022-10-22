//package com.ck.usercenter.service.impl;
//
//import com.ck.usercenter.mapper.UserMapper;
//import com.ck.usercenter.model.domain.User;
//import com.ck.usercenter.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.StopWatch;
//
//import javax.annotation.Resource;
//
//import java.io.Serializable;
//import java.util.*;
//import java.util.concurrent.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class UserServiceImplTest  {
//
//    @Resource
//    private UserService userService;
//
//    private ExecutorService executorService = new ThreadPoolExecutor(60, 100, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
//
//    @Test
//    public void doInsertUsers(){
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        int batchSize = 10000;
//        int j = 0;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            ArrayList<User> userList = new ArrayList<>();
//            while(true) {
//                j++;
//                User user = new User();
//                user.setUsername("用户名-假数据");
//                user.setUserAccount("userAccountFake");
//                user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
//                user.setGender((byte) 0);
//                user.setUserPassword("12345678");
//                user.setPhone("123");
//                user.setEmail("123@qq.com");
//                user.setUserStatus(0);
//                user.setUserRole(0);
//                user.setPlanetCode("878787");
//                user.setTags("[\"female\", \"前端\", \"working\", \"Java\"]");
//                userList.add(user);
//                if (j % batchSize == 0){
//                    break;
//                }
//            }
//            //异步执行
//            //异步任务，executorService是所制定的线程池
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                userService.saveBatch(userList, batchSize);
//            }, executorService);
//            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//}