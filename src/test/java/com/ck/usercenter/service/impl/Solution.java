//package com.ck.usercenter.service.impl;
//
//
//import java.util.*;
//
//class Solution {
//    public static void main(String[] args) {
//        System.out.println(generateParenthesis(3));
//    }
//    public static List<String> generateParenthesis(int n) {
//        List<String> result = new ArrayList<>();
//        dfs("", n , n, result);
//        return result;
//    }
//    public static void dfs(String presentResult, int left, int right, List<String> result){
//        if (left == 0 && right == 0) {
//            result.add(presentResult);
//            return;
//        }
//        if (left > right) return;
//        if (left > 0){
//            dfs(presentResult + "(", left -1, right, result);
//        }
//        if (right > 0){
//            dfs(presentResult + ")", left, right - 1, result);
//        }
//    }
//}
////小卡买鸡蛋
////        完数
////        斐波那契数列
