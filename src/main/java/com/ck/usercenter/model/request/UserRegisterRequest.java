package com.ck.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户注册请求体
 *
 * @author ck
 */

@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -5359610537562554361L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
//    private String planetCode;
}
