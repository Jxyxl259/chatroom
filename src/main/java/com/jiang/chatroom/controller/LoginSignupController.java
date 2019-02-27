package com.jiang.chatroom.controller;

import com.jiang.chatroom.common.RequestResult;
import com.jiang.chatroom.common.enums.GlobalMessageEnum;
import com.jiang.chatroom.common.util.IpAddressUtil;
import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginSignupController {

    private static Logger log = LoggerFactory.getLogger(LoginSignupController.class);

    @Autowired
    private UserService userService;
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
            //Jedis jedis = jedisConfig.getConnection();

            // 缓存用户登录后的IP地址
            // jedis.hset(userIpAddrHashKey, String.valueOf(user.getId()), userIp);
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
    public RequestResult<Boolean> signUp(User u, HttpSession session){
        //log.debug("signUp user={}", u);
        RequestResult<Boolean> result = new RequestResult<>(true,true);

        String username = u.getUserName();
        String password = u.getUserPassword();

        boolean success = userService.userSignUp(u);
        if(success){
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            session.setAttribute("user", u);
        } else{
            log.error("用户注册失败");
        }
        return result;
    }



}