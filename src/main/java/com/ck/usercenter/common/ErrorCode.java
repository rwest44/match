package com.ck.usercenter.common;

public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    FAIL(1, "fail",  ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    private final int code;
    private final String message;
    private final String description;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    ErrorCode(int code, String message, String description) {

        this.code = code;

        /**
         * 状态码信息
         */
        this.message = message;

        /**
         * 状态码描述（详情）
         */
        this.description = description;

    }
}
