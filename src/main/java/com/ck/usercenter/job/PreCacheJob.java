package com.ck.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ck.usercenter.mapper.ActiveUserMapper;
import com.ck.usercenter.model.domain.ActiveUser;
import com.ck.usercenter.model.domain.User;
import com.ck.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热任务
 *
 * @author ck
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private ActiveUserMapper activeUserMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private List<ActiveUser> getMainUserList(){
        QueryWrapper<ActiveUser> queryWrapper = new QueryWrapper();
        // 重活跃点用户
        return activeUserMapper.selectList(queryWrapper);
    }

    // 每天执行，预热推荐用户
    @Scheduled(cron = "59 59 23 * * ?")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("match:preCacheJob:doCacheRecommendUser:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock: " + Thread.currentThread().getId());
                System.out.println("开始执行推荐用户缓存预热");
                //遍历活跃用户表
                List<ActiveUser> mainUserList = getMainUserList();
                for (ActiveUser mainUser : mainUserList) {
                    QueryWrapper<User> queryWrapperUser = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapperUser);
                    String redisKey = String.format("match:user:recommend:%s", mainUser.getUserId());
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    // 写缓存
                    try {
                        valueOperations.set(redisKey, userPage, 1, TimeUnit.DAYS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }

    // 每天执行，为重点活跃用户提前缓存心动模式匹配结果
    @Scheduled(cron = "0 30 0 * * ?")
    public void doCacheMatchUser() {
        RLock lock = redissonClient.getLock("match:preCacheJob:doCacheMatchUser:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock: " + Thread.currentThread().getId());
                System.out.println("开始执行心动模式匹配用户缓存预热");
                //遍历活跃用户表
                List<ActiveUser> mainUserList = getMainUserList();
                for (ActiveUser mainUser : mainUserList) {
                    QueryWrapper<User> queryWrapperUser = new QueryWrapper<>();

                    User user = new User();
                    BeanUtils.copyProperties(mainUser, user);
                    user.setId(mainUser.getUserId());

                    List<User> matchedUser = userService.matchUsers(20, user);

                    String redisKey = String.format("match:user:match:%s", mainUser.getUserId());
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    // 写缓存
                    try {
                        valueOperations.set(redisKey, matchedUser, 1, TimeUnit.DAYS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheMatchUser error", e);
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }


}
