package com.jiang.chatroom.config.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 开启WebSocket支持
 *
 * @author :ZHANGPENGFEI
 * @create 2018-07-11 13:52
 **/
@Configuration(value="webSocketConfig")
@ConfigurationProperties(prefix = "web-socket")
public class WebSocketConfig {



    private String webSocketServerIpPort;



    /**
     * 使用嵌入式Servlet容器启动项目时需要以下代码，使用外置Tomcat容器启动项目时注释掉下面代码，否则报错
     * Multiple Endpoints may not be deployed to the same path
     * @return
     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }


    public String getWebSocketServerIpPort() {
        return webSocketServerIpPort;
    }

    public void setWebSocketServerIpPort(String webSocketServerIpPort) {
        this.webSocketServerIpPort = webSocketServerIpPort;
    }

    @Override
    public String toString() {
        return "WebSocketConfig{" +
                "ServerIpPort='" + webSocketServerIpPort + '\'' +
                '}';
    }
}
