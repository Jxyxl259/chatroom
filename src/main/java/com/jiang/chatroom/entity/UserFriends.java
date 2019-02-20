package com.jiang.chatroom.entity;

public class UserFriends {
    private Long id;

    private Long userId;

    private String userFriendIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFriendIds() {
        return userFriendIds;
    }

    public void setUserFriendIds(String userFriendIds) {
        this.userFriendIds = userFriendIds == null ? null : userFriendIds.trim();
    }
}