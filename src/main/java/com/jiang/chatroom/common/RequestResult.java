package com.jiang.chatroom.common;

/**
 * @description: 请求结果
 * @author: jiangxy
 * @create: 2018-07-07 22:33
 */
public class RequestResult<T> {

    protected Boolean success;

    protected String status;

    protected String statusCode;

    protected String message;

    /**
     * 返回值，类型为T
     */
    private T t;

    public RequestResult() {
    }

    public RequestResult(boolean success) {
        this.success = success;
    }

    public RequestResult(T t, boolean success) {
        this.success = success;
        this.t = t;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 返回值，类型为T
     */
    public T getT() {
        return t;
    }

    /**
     * 返回值，类型为T
     */
    public void setT(T t) {
        this.t = t;
    }




}
