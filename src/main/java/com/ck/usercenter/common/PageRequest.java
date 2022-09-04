package com.ck.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求参数
 *
 * @author yupi
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -2984898589343713074L;

    /**
     * 页面大小
     */
    protected int pageSize = 10;

    /**
     * 当前是第几页
     */
    protected int pageNum = 1;
}
