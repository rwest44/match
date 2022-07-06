package com.ck.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户登录请求体
 *
 * @author ck
 */

@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 809297924422525899L;

    private String userAccount;
    private String userPassword;

}
