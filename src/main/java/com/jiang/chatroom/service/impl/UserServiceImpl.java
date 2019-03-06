package com.jiang.chatroom.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Iterables;
import com.jiang.chatroom.common.RequestResult;
import com.jiang.chatroom.dao.UserMapper;
import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.service.UserService;
import com.jiang.chatroom.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jiang.chatroom.common.enums.GlobalMessageEnum.USER_EXIST;

/**
 * @description: 用户服务层实现
 * @author: jiangBUG@outlook.com
 * @create: 2019-02-20 19:22
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static String MD5 = "MD5";

    private static int HASH_TIMES = 2;

    @Autowired
    private UserMapper userMapper;


    @Override
    public User getUserByUsername(String username) {
        log.info("getUserByUsername param username:{}", username);
        User user = userMapper.selectByUserName(username);
        log.info("getUserByUsername return{}", JSON.toJSONString(user));
        return user;
    }

    @Override
    public User getUserInfoByUserId(Long id) {
        log.info("getUserByUsername param userId:{}", id);
        User user = userMapper.selectUserInfoByUserId(id);
        log.info("getUserByUsername return{}", JSON.toJSONString(user));
        return user;
    }

    @Override
    public UserVo getUserFriendList(Long userId) {

        UserVo userVo = new UserVo();
        List<User> friends = userMapper.getUserFriendList(userId);
        // Iterables 非Iterators
        Iterables.removeIf(friends, (u) -> StringUtils.isEmpty(u.getUserName()));
        userVo.setFriend(friends);
        return userVo;
    }

    @Override
    public RequestResult<String> userSignUp(User u) {

        RequestResult<String> result = new RequestResult<>(false);

        User user = userMapper.selectByUserName(u.getUserName());
        if(user != null){
            result.setStatusCode(USER_EXIST.getCode());
            result.setMessage(USER_EXIST.getMessage());
            result.setT(USER_EXIST.getMessage());
            return result;
        }

        String passwordBeforeEncrypt = u.getUserPassword();
        ByteSource credentialsSalt = ByteSource.Util.bytes(u.getUserName());
        SimpleHash simpleHash = new SimpleHash(
                MD5,
                passwordBeforeEncrypt,
                credentialsSalt,
                HASH_TIMES);

        u.setUserPassword(simpleHash.toString());
        int insert = userMapper.insert(u);
        result.setSuccess(true);
        return result;
    }


    @Override
    public RequestResult<String> addfriend(User user, String searchName) {
        RequestResult<String> result = new RequestResult<>(false);
        User dest = userMapper.selectByUserName(searchName);
        if(dest != null){
            // TODO 先判断用户是否已经是好友
            // ...

            //用户存在，可添加好友
            int affectRow = userMapper.insertFriendRelationship(user.getId(), dest.getId());
            if(affectRow == 1 ){
                return new RequestResult<>("用户添加好友成功", true);
            }
        }else{
            result.setMessage("用户不存在");
        }

        return result;
    }
}
