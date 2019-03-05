package com.jiang.chatroom.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author:
 * @create: 2019-03-05 11:16
 */

@Configuration
public class ServletContextConfig {


    /**
     * 配置servlet容器监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean myServletContextListener(){
        ServletListenerRegistrationBean<MyServletContextListener> registrationBean = new ServletListenerRegistrationBean(new MyServletContextListener());
        return registrationBean;
    }

}
