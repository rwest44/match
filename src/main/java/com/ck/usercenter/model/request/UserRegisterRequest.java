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


    private static final long serialVersionUID = 809297924422525899L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
