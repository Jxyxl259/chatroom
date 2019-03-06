package com.jiang.chat_room;

import com.jiang.chatroom.config.websocket.WebSocketConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot01HelloworldQuickApplicationTests {

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Test
    public void contextLoads() {

        System.out.println(webSocketConfig);

    }

}