package com.jiang.chatroom.dao;

import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.entity.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> getUserFriendList(Long userId);

    /**
     * 根据用户昵称获取用户信息
     * @param username
     * @return
     */
    User selectByUserName(String username);


    /**
     * 根据用户Id获取用户信息
     * @param id
     * @return
     */
    User selectUserInfoByUserId(Long id);
}