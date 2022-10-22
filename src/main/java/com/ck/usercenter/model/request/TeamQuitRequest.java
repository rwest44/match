package com.ck.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍请求体
 *
 * @author yupi
 */
@Data
public class TeamQuitRequest implements Serializable {


    private static final long serialVersionUID = 6697838993143129714L;
    /**
     * id
     */
    private Long teamId;

}
