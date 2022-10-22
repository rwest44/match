package com.ck.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 活跃用户
 * @TableName user_active
 */
@TableName(value ="user_active")
@Data
public class ActiveUser implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private long id;

    /**
     * 用户Id
     */
    private long userId;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 星球编号
     */
    private String planetCode;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 上次登录时间
     */
    private Date lastLogin;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;


    /**
     * 用户标签 json
     */
    private String tags;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}