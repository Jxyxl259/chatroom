package com.jiang.chatroom.entity;


import com.alibaba.fastjson.JSON;
import com.jiang.chatroom.config.websocket.WebSocketServer;
import com.jiang.chatroom.entity.chat.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: jiangbug@outlook.com
 * @create: 2019-02-26 16:49
 */
@ServerEndpoint(value = "/dispatcher/{userName}")
@Component
@Scope("prototype")
public class SingleChattingSock {

    private static Logger log = LoggerFactory.getLogger(SingleChattingSock.class);


    // 与某个 Browser 的连接会话，需要通过它来给客户端发送数据
    private Session webSocketSession;

    // 当前发消息的用户名
    private String userName = "";


    /**
     * 连接建立成功调用的方法
     *
     * @param socketSession 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userName") String userName, Session socketSession, EndpointConfig config) {
        log.info(userName);
        this.userName = userName;//接收到发送消息的人员编号
        this.webSocketSession = socketSession;
        WebSocketServer.getSocks().put(userName, this);//加入map中
        //addOnlineCount();  //在线数加1
        log.info("userName:{} connect to web webSocketServer !", userName);
    }


    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose() {
        if (!this.userName.equals("")) {
            // 将socketSession从Map中移除
            WebSocketServer.getSocks().remove(userName);
            log.info("userName:{} disconnect from the server", userName);
        }
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @SuppressWarnings("unused")
    @Deprecated
//	@OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        //群发消息
        //sendAll(message);

        //给指定的人发消息
        sendToUser(message);
    }


    /**
     * 给指定好友发送消息
     * @param sockMsg 指定好友名称和要发送的消息  
     */
    @OnMessage
    public void sendToUser(String sockMsg) {
        Message message = JSON.parseObject(sockMsg, Message.class);
        String destUserno = message.getContactName();
        String sendMessage = message.getMsg();
        String now = getNowTime();
        try {
            if (WebSocketServer.getSocks().get(destUserno) != null) {
                Message msg = new Message(sendMessage, this.userName);
                String sockMsgJson = JSON.toJSONString(msg);
//                WebSocketServer.getSocks().get(destUserno).sendMessage(now + "用户" + this.userName + "发来消息：" + " <br/> " + sendMessage);
                WebSocketServer.getSocks().get(destUserno).sendMessage(sockMsgJson);
            } else {
                // TODO 对方不在线，将消息放到 MQ中
                log.info("user:{} offLine", destUserno);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 给所有人发消息
     * @param message
     */
    @SuppressWarnings("Unused")
    private void sendAll(String message) {
        String now = getNowTime();
        String sendMessage = message;
        //遍历HashMap
        for (String key : WebSocketServer.getSocks().keySet()) {
            try {
                //判断接收用户是否是当前发消息的用户
                if (!userName.equals(key)) {
                    WebSocketServer.getSocks().get(key).sendMessage(now + "用户" + userName + "发来消息：" + " <br/> " + sendMessage);
                    System.out.println("key = " + key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    private String getNowTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException  
     */
    public void sendMessage(String message) throws IOException {
        this.webSocketSession.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


}
