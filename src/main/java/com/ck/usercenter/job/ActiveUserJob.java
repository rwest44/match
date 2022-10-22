package com.ck.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ck.usercenter.mapper.ActiveUserMapper;
import com.ck.usercenter.model.domain.ActiveUser;
import com.ck.usercenter.model.domain.User;
import com.ck.usercenter.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 重点活跃用户更新任务
 *
 * @author ck
 */
@Component
@Slf4j
public class ActiveUserJob {

    @Resource
    private ActiveUserMapper activeUserMapper;

    @Resource
    private RedissonClient redissonClient;

    private List<ActiveUser> getMainUserList(){
        QueryWrapper<ActiveUser> queryWrapper = new QueryWrapper();
        // 重活跃点用户
        return activeUserMapper.selectList(queryWrapper);
    }

    // 每天执行，3天内未活跃的用户不再为重点活跃用户
    @Scheduled(cron = "0 50 23 * * ?")
    public void doUpdateActiveUser() {
        RLock lock = redissonClient.getLock("match:ActiveUser:doUpdateActiveUser:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock: " + Thread.currentThread().getId());
                System.out.println("开始执行更新重点活跃用户表");
                //遍历活跃用户表
                List<ActiveUser> mainUserList = getMainUserList();
                for (ActiveUser mainUser : mainUserList) {

                    Date lastLogin = mainUser.getLastLogin();
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(lastLogin);
                    calendar.add(Calendar.DATE, 3);
                    lastLogin = calendar.getTime();
                    Date today = new Date();
                    if (today.after(lastLogin)){
                        activeUserMapper.deleteById(mainUser);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doUpdateActiveUser error", e);
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }

}
