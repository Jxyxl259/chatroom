package com.jiang.chatroom.config.websocket;

import org.springframework.context.annotation.Configuration;

/**
 * 开启WebSocket支持
 *
 * @author :ZHANGPENGFEI
 * @create 2018-07-11 13:52
 **/
@Configuration
public class WebSocketConfig {

    /**
     * 使用嵌入式Servlet容器启动项目时需要以下代码，使用外置Tomcat容器启动项目时注释掉下面代码，否则报错
     * Multiple Endpoints may not be deployed to the same path
     * @return
     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
}
