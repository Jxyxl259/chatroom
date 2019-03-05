package com.jiang.chatroom.config.shiro.filter;

import com.jiang.chatroom.entity.User;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashSet;

/**
 * @description: 扩展用户登出过滤器
 * @author: jiangbug@outlook.com
 * @create: 2019-02-20 23:51
 */
public class MyLogoutFilter extends LogoutFilter {


//    private JedisPool pool;
//
//    public MyLogoutFilter(JedisPool pool){
//        this.pool = pool;
//    }

    /**
     * 用户登出时将存在redis中的用户IP地址移除掉
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        User user = (User) subject.getPrincipals().getPrimaryPrincipal();

        //Jedis jedis = pool.getResource();
        //jedis.hdel(userIpAddrHashKey, String.valueOf(user.getId()));

        // 用户登出时，从在线用户列表中移除用户
        HashSet<String> onlineUserNames = (HashSet<String>)request.getServletContext().getAttribute("ONLINE_USERS");
        onlineUserNames.remove(user.getUserName());
        return super.preHandle(request, response);
    }

//    public void setPool(JedisPool pool) {
//        this.pool = pool;
//    }
}
