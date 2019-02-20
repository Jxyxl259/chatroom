package com.jiang.chatroom.common;

/**
 * @ClassName Result
 * @Description
 * @Author jiangxy
 * @Date 2018\11\16 0016 14:38
 * @Version 1.0.0
 */
public class BaseResult<T> {

    protected Boolean success;

    protected String status;

    protected String statusCode;

    protected String message;

    private T t;

    public BaseResult() {
    }

    public BaseResult(Boolean success, String statusCode) {
        this.success = success;
        this.statusCode = statusCode;
    }

    public BaseResult(Boolean success, T t) {
        this.success = success;
        this.t = t;
    }

    public BaseResult(Boolean success, String statusCode, String message, T t) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.t = t;
    }

    public BaseResult(Boolean success, String statusCode, String message) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
    }

    public BaseResult(Boolean success, String status, String statusCode, String message) {
        this.success = success;
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
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

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
