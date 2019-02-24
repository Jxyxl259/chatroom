package com.jiang.chatroom.config.shiro.filter;

import com.jiang.chatroom.entity.User;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static com.jiang.chatroom.common.util.RedisKeyUtil.userIpAddrHashKey;

/**
 * @description: 扩展用户登出过滤器
 * @author: jiangbug@outlook.com
 * @create: 2019-02-20 23:51
 */
public class MyLogoutFilter extends LogoutFilter {


    private JedisPool pool;

    public MyLogoutFilter(JedisPool pool){
        this.pool = pool;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        User user = (User) subject.getPrincipals().getPrimaryPrincipal();

        Jedis jedis = pool.getResource();
        jedis.hdel(userIpAddrHashKey, String.valueOf(user.getId()));
        return super.preHandle(request, response);
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }
}
