package com.jiang.chatroom.service;

import com.jiang.chatroom.common.RequestResult;
import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.vo.UserVo;

/**
 * @description: 用户服务层
 * @author: jiangBUG@outlook.com
 * @create: 2019-02-20 19:20
 */
public interface UserService {


    /**
     * 根据与用户名查询用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);


    /**
     * 根据用户id查询用户详细信息
     * @param id
     * @return
     */
    User getUserInfoByUserId(Long id);


    /**
     * 获取用户好友列表
     * @param userId
     * @return
     */
    UserVo getUserFriendList(Long userId);

    /**
     * 用户注册
     * @param u
     * @return
     */
    RequestResult<String> userSignUp(User u);

    /**
     * 用户添加好友
     * @param user
     * @param searchName
     * @return
     */
    RequestResult<String> addfriend(User user, String searchName);
}
