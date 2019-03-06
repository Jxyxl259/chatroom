package com.jiang.chatroom.controller;

import com.alibaba.fastjson.JSON;
import com.jiang.chatroom.common.RequestResult;
import com.jiang.chatroom.common.enums.GlobalMessageEnum;
import com.jiang.chatroom.common.util.IpAddressUtil;
import com.jiang.chatroom.config.websocket.WebSocketConfig;
import com.jiang.chatroom.config.websocket.WebSocketServer;
import com.jiang.chatroom.entity.SingleChattingSock;
import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.entity.chat.Message;
import com.jiang.chatroom.service.UserService;
import com.jiang.chatroom.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

@Controller
public class LoginSignupController {

    private static Logger log = LoggerFactory.getLogger(LoginSignupController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @GetMapping("/getServerAddr")
    @ResponseBody
    public RequestResult<String> serverAddr(){
        return new RequestResult<>(webSocketConfig.getWebSocketServerIpPort(), true);
    }

//    @Autowired
//    private JedisConfig jedisConfig;


    @PostMapping("/doLogin")
    @ResponseBody
    public RequestResult<Boolean> login(User u, HttpSession session, HttpServletRequest request) {

        log.debug("login user={}", u);
        RequestResult<Boolean> result = new RequestResult<>(true, true);

        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            return result;
        }

        String username = u.getUserName();
        String password = u.getUserPassword();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            User user = (User) subject.getPrincipals().getPrimaryPrincipal();
            session.setAttribute("user", user);
            String userIp = IpAddressUtil.getIpAdrress(request);
            log.info("用户IP地址{}", userIp);
            HashSet<String> onlineUserNames = (HashSet<String>)session.getServletContext().getAttribute("ONLINE_USERS");
            onlineUserNames.add(user.getUserName());
            //Jedis jedis = jedisConfig.getConnection();

            // 缓存用户登录后的IP地址
            // jedis.hset(userIpAddrHashKey, String.valueOf(user.getId()), userIp);

            // 登录后通知好友 上线信息
            UserVo userVo = userService.getUserFriendList(user.getId());

            if(!CollectionUtils.isEmpty(userVo.getFriend())){
                userVo.getFriend().forEach(f ->{
                    if(onlineUserNames.contains(f.getUserName())){
                        // 一个用户登入，通知浏览器client端有该用户好友的用户 该好友上线
                        try {
                            user.setOnline(true);
                            Map<String, SingleChattingSock> socks = WebSocketServer.getSocks();
                            String needNotifyUserName = f.getUserName();
                            log.info("user {} login, needNotify his friend:{}", user.getUserName(), needNotifyUserName);
                            SingleChattingSock sock = socks.get(needNotifyUserName);
                            if(sock != null && !org.apache.commons.lang3.StringUtils.isEmpty(needNotifyUserName)) {
                                String userOnlineMsg = JSON.toJSONString(new Message(user.toString(), "system", GlobalMessageEnum.SYSTEM.getCode()));
                                sock.sendMessage(userOnlineMsg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("用户登陆时通知好友，好友已上线，web-socket IO 异常", e);
                        }
                    }
                });
            }
        } catch (ExcessiveAttemptsException e) {
            result.setSuccess(false);
            result.setT(false);
            result.setMessage(GlobalMessageEnum.EXCESSIVE_LOGIN_TYR.getMessage());
        } catch (UnknownAccountException e) {
            result.setSuccess(false);
            result.setT(false);
            result.setMessage(GlobalMessageEnum.NO_SUCH_ACCOUNT.getMessage());
        } catch (CredentialsException e) {
            result.setSuccess(false);
            result.setT(false);
            result.setMessage(GlobalMessageEnum.ERROR_USERNAME_OR_PASSWORD.getMessage());
        } catch (AuthenticationException e) {
            result.setSuccess(false);
            result.setT(false);
            result.setMessage(GlobalMessageEnum.AUTHENTICATION_ERROR.getMessage());
        }

        return result;
    }

    /**
     * 用户注册
     * @param u
     * @param session
     * @return
     */
    @PostMapping("/doSignup")
    @ResponseBody
    public RequestResult<String> signUp(User u, HttpSession session){
        //log.debug("signUp user={}", u);
        RequestResult<String> result = new RequestResult<>(true);

        String username = u.getUserName();
        String password = u.getUserPassword();

        result = userService.userSignUp(u);
        if(result.getSuccess()){
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            session.setAttribute("user", u);
        } else{
            log.error("用户注册失败, 原因：{}", result.getMessage());
        }
        return result;
    }



    @RequestMapping("/")
    public String homePage(){
        return "homepage";
    }

}