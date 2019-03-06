package com.jiang.chatroom.common.enums;

public enum GlobalMessageEnum {
    SUCCESS("000000", "操作成功"),
    EMPTY_DATA("111111", "未查询到相关数据"),
    PARAM_IS_NULL("999998", "参数为空"),
    FAILED("999999", "操作失败"),
    SYSTEM_ERROR("999999", "系统错误"),


    //-------------- login ------------------
    LOGIN_SUCCESS("000001","登陆成功"),
    EXCESSIVE_LOGIN_TYR("111200","密码尝试次数过多"),
    NO_SUCH_ACCOUNT("111201", "该用户不存在"),
    ERROR_USERNAME_OR_PASSWORD("111202", "用户名或密码错误"),
    AUTHENTICATION_ERROR("111203","用户认证错误"),
    //-------------- login ------------------

    //-------------- sign up ---------------------
    USER_EXIST("111300", "用户名已被占用"),
    //-------------- sign up ---------------------


    //------------------- message enum --------------------
    USER("0","好友消息"),
    SYSTEM("1","系统消息");
    //------------------- message enum --------------------


    private String code;
    private String message;

    GlobalMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
