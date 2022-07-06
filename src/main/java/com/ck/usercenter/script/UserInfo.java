package com.ck.usercenter.script;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户信息
 */
@Data
@EqualsAndHashCode
public class UserInfo {
    /**
     * 星球编号
     */
    @ExcelProperty("成员编号")
    private String planetCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("昵称")
    private String userName;

}