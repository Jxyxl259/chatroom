package com.jiang.chatroom.common;

import com.jiang.chatroom.common.enums.GlobalMessageEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: RequestResult工厂类
 * @author: jiangxy
 * @create: 2018-07-07 22:37
 */
public class RequestResultFactory {


    private static RequestResult sucess = new RequestResult(true);

    static {
        sucess.setStatusCode(String.valueOf(GlobalMessageEnum.SUCCESS.getStatusCode()));
        sucess.setMessage(GlobalMessageEnum.SUCCESS.getMessage());
    }


    public static RequestResult success(){
        return sucess;
    }


    public static <T> RequestResult<T> success(T t){
        RequestResult res = new RequestResult(true);
        res.setMessage(GlobalMessageEnum.SUCCESS.getMessage());
        res.setStatusCode(GlobalMessageEnum.SUCCESS.getStatusCode());
        res.setT(t);
        return res;
    }

    public static <T> RequestResult<T> success(T t, String message){
        RequestResult res = new RequestResult(true);
        res.setMessage(message);
        res.setStatusCode(GlobalMessageEnum.SUCCESS.getStatusCode());
        res.setT(t);
        return res;
    }

    public static RequestResult faild(String faildReason){
        RequestResult res = new RequestResult(false);
        res.setMessage(faildReason);
        return res;
    }


    public static RequestResult failed(GlobalMessageEnum Global){
        RequestResult res = new RequestResult(false);
        res.setStatusCode(Global.getStatusCode());
        res.setMessage(Global.getMessage());
        return res;
    }

    public static RequestResult failed(GlobalMessageEnum Global, String faildReason){
        RequestResult res = new RequestResult(false);
        res.setStatusCode(Global.getStatusCode());
        res.setMessage(Global.getMessage());
        if(StringUtils.isNotBlank(faildReason)){
            res.setMessage(faildReason);
        }
        return res;
    }

    public static RequestResult failed(String statusCode, String faildReason){
        RequestResult res = new RequestResult(false);
        res.setStatusCode(statusCode);
        res.setMessage(faildReason);
        return res;
    }

}
