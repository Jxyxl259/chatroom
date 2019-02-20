package com.jiang.chatroom.dao;

import com.jiang.chatroom.entity.UserIp;
import com.jiang.chatroom.entity.UserIpExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserIpMapper {
    int countByExample(UserIpExample example);

    int deleteByExample(UserIpExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserIp record);

    int insertSelective(UserIp record);

    List<UserIp> selectByExample(UserIpExample example);

    UserIp selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserIp record, @Param("example") UserIpExample example);

    int updateByExample(@Param("record") UserIp record, @Param("example") UserIpExample example);

    int updateByPrimaryKeySelective(UserIp record);

    int updateByPrimaryKey(UserIp record);
}