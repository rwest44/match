package com.ck.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户加入队伍请求体
 *
 * @author yupi
 */
@Data
public class TeamJoinRequest implements Serializable {


    private static final long serialVersionUID = 1786765064432620135L;
    /**
     * id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
