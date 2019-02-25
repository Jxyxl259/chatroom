package com.jiang.chatroom.controller;

import com.alibaba.fastjson.JSON;
import com.jiang.chatroom.common.RequestResult;
import com.jiang.chatroom.common.RequestResultFactory;
import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.service.UserService;
import com.jiang.chatroom.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 用户相关的控制器
 * @author: jiangxy
 * @create: 2018-07-10 23:20
 */
@RestController
@RequestMapping("/user")
public class UserController {

    public static Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @PostMapping("/userHeaderIcon/{username}")
    public RequestResult<User> getUserDetail(@PathVariable("username") String username){
        log.debug("getUserDetail param={}", username);

        User user = userService.getUserByUsername(username);

        log.debug(user == null ? "no such user named " + username  + "!" : "getUserDetail result={" + JSON.toJSONString(user) + "}");

        if(user != null){
            return RequestResultFactory.success(user);
        }else{
            return new RequestResult<User>(false);
        }
    }


    /**
     * 拉取用户好友列表
     * @param request
     * @return
     */
    @PostMapping("/fetchUserFriendsList")
    public RequestResult getUserPerms(HttpServletRequest request){
        RequestResult result = new RequestResult(false);
        User u = (User)request.getSession().getAttribute("user");
        if(u == null){
            result.setMessage("用户未登陆");
            result.setStatus("9999");
            return result;
        }
        Long userId = u.getId();
        UserVo userVo  = null;
        try {
            userVo = userService.getUserFriendList(userId);
        } catch (Exception e) {
            log.error("拉取好友列表 userService.getUserFriendList 异常, cause:{} message:{}", e.getCause(), e.getMessage());
            result.setMessage("拉取好友列表异常，请联系管理员");
            result.setStatus("9999");
            e.printStackTrace();
            return result;
        }
        return RequestResultFactory.success(userVo);
    }



}
