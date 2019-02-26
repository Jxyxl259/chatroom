package com.jiang.chatroom.config.websocket;

import com.jiang.chatroom.entity.SingleChattingSock;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: WebSocket服务端——所有的与Browser的webSocket连接都保存于该类
 * @author: jiangbug@outlook.com
 * @create: 2019-02-26 20:34
 */
@Component("webSocketServer")
@Scope("singleton")
public class WebSocketServer {

    private static Map<String, SingleChattingSock> socks = new ConcurrentHashMap<>();



    public static Map<String, SingleChattingSock> getSocks() {
        return socks;
    }
}
