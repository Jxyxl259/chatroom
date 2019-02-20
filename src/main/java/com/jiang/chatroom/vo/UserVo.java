package com.jiang.chatroom.vo;

import com.jiang.chatroom.entity.User;

import java.util.List;

/**
 * @description: 用户渲染
 * @author: jiangBUG@outlook.com
 * @create: 2019-02-20 19:39
 */
public class UserVo extends User {


    /**
     * 好友列表
     */
    private List<User> friend;

    public List<User> getFriend() {
        return friend;
    }

    public void setFriend(List<User> friend) {
        this.friend = friend;
    }
}
