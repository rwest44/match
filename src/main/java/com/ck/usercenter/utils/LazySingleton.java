package com.ck.usercenter.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 双检索单例模式
 *
 * @author ck
 */

public class LazySingleton {
    //test
    // 单例对象 ,加入volatile关键字进行修饰
    private static volatile LazySingleton loginCount;
    // 计数器
    private AtomicLong count = new AtomicLong(0);

    private LazySingleton() { }
    public static LazySingleton getInstance() {
        if (loginCount == null) {
            // 对类进行加锁，并进行双重检查
            synchronized (LazySingleton.class) {
                if (loginCount == null) {
                    loginCount = new LazySingleton();
                }
            }
        }
        return loginCount;
    }
    public AtomicLong getCount() {
        return count;
    }
    public void addCount() {
        count.addAndGet(1);
    }
    public void reduceCount(){
        count.addAndGet(-1);
    }
}

