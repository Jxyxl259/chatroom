package com.jiang.chatroom.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description: SpringBean工具类
 * @author: jiangxy
 * @create: 2018-07-10 21:31
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据BeanName获取IOC容器中的对象
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        Object bean = applicationContext.getBean(beanName);
        return bean;
    }


    /**
     * 根据BeanType获取IOC容器中的对象
     * @param clazz
     * @return
     */
    public Object getBeanByClass(Class clazz){
        Object bean = applicationContext.getBean(clazz);
        return bean;
    }

}
