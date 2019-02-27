package com.jiang.chatroom.controller;

import com.jiang.chatroom.common.RequestResult;
import com.jiang.chatroom.common.util.RedisKeyUtil;
import com.jiang.chatroom.config.redis.JedisConfig;
import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.entity.chat.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 消息接收与web-socket 传输
 * @author: jiangBUG@outlook.com
 * @create: 2019-02-26 12:46
 */
@Controller
@RequestMapping("/chat")
public class ChatController {

    private static Logger log = LoggerFactory.getLogger(ChatController.class);


    @Autowired
    private JedisConfig jedisConfig;

    @Deprecated
    @ResponseBody
    @RequestMapping(value=("/msg/receive"), consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public RequestResult<String> reciveMsg(@RequestBody Message msg, HttpServletRequest request){
        User u = (User)request.getSession().getAttribute("user");
        String sendMsgUser = u.getUserName();
        String msgDestinationIpAddr = jedisConfig.getConnection().hget(RedisKeyUtil.userIpAddrHashKey, msg.getContactName());
        if(StringUtils.isEmpty(msgDestinationIpAddr)){
            log.info("user {} offline, put the msg into MessageQueue", msg.getContactName());
            // TODO 对于对方没有在线的，放入消息队列
            RequestResult<String> resp = new RequestResult<String>(true);
            resp.setMessage("offline");
            resp.setStatus("900001");
            return resp;
        }

        log.info("receiveMsg -> {}, sendMsgUser -> {}, receiveMsgUser", msg, sendMsgUser, msgDestinationIpAddr);
        // websocket

        RequestResult<String> resp = new RequestResult<String>(true);
        resp.setMessage("success");
        resp.setStatus("000000");
        return resp;
    }


}
