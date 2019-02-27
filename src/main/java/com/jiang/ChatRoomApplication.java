package com.jiang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({
        "com.jiang.chatroom"
})
@MapperScan({
        "com.jiang.**.dao"
})
public class ChatRoomApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ChatRoomApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ChatRoomApplication.class);
    }
}
