package com.jiang.chatroom.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;

/**
 * @description: servlet容器监听
 * @author: jiangbug@outlook.com
 * @create: 2019-03-05 11:10
 */
public class MyServletContextListener  implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        HashSet<String> onlineUserName = new HashSet<>();
        servletContextEvent.getServletContext().setAttribute("ONLINE_USERS", onlineUserName );
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
