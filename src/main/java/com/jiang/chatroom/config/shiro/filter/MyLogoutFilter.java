package com.jiang.chatroom.config.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.jiang.chatroom.common.enums.GlobalMessageEnum;
import com.jiang.chatroom.config.websocket.WebSocketServer;
import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.entity.chat.Message;
import com.jiang.chatroom.service.UserService;
import com.jiang.chatroom.vo.UserVo;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
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

    private UserService userService;

    public MyLogoutFilter(UserService userService){
        this.userService = userService;
    }

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

        // 用户登出时，从在线用户列表中移除用户,
        HashSet<String> onlineUserNames = (HashSet<String>)request.getServletContext().getAttribute("ONLINE_USERS");
        onlineUserNames.remove(user.getUserName());

        UserVo userVo = userService.getUserFriendList(user.getId());

        if(!CollectionUtils.isEmpty(userVo.getFriend())){
            userVo.getFriend().forEach(f ->{
                if(onlineUserNames.contains(f.getUserName())){
                    // 一个用户登出，通知浏览器client端有该用户好友的用户 该好友下线
                    try {
                        user.setOnline(false);
                        WebSocketServer.getSocks().get(f.getUserName()).sendMessage(JSON.toJSONString(new Message(user.toString(), "system", GlobalMessageEnum.SYSTEM.getCode())));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("用户登出时通知好友该好友已下线，web-socket IO 异常", e);
                    }
                }
            });
        }


        return super.preHandle(request, response);
    }

//    public void setPool(JedisPool pool) {
//        this.pool = pool;
//    }




    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
