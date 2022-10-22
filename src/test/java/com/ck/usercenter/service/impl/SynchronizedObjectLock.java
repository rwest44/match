//package com.ck.usercenter.service.impl;
//
//
//import javax.xml.crypto.Data;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//
//public class SynchronizedObjectLock {
//    public static void main(String[] args) {
//
//        // 获取当前时间
//        Date date = new Date();
//        System.out.println(date);
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(date);
//        // 把日期往后增加一天,整数  往后推,负数往前移动
//        calendar.add(Calendar.DATE, 1);
//        // 这个时间就是日期往后推一天的结果
//        Date earlyDate = new Date();
//        Date laterDate = new Date();
//        earlyDate = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        laterDate = calendar.getTime();
//        System.out.println(earlyDate);
//        System.out.println(laterDate);
//        System.out.println(earlyDate.after(laterDate));
//
//
//    }
//
//}