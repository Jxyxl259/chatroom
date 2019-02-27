package com.jiang.chatroom.config.shiro;

import com.jiang.chatroom.entity.User;
import com.jiang.chatroom.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: shiro认证/授权功能实现类
 * @author: jiangxy
 * @create: 2018-07-08 14:30
 */
public class MyShiroRealm extends AuthorizingRealm{

    private static Logger log = LoggerFactory.getLogger(MyShiroRealm.class);

    // @Autowired
    private UserService userService;

    /**
     * 实现授权功能
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // TODO 授权访问控制
        return null;
    }


    /**
     * 实现认证功能(认证用户登陆)
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String username = token.getUsername();
        User user = userService.getUserByUsername(username);
        if(user == null){
            throw new UnknownAccountException();
        }else{
            user = userService.getUserInfoByUserId(user.getId());
        }

        // 使用 userName作为加密盐值
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUserName());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,
                user.getUserPassword(),
                credentialsSalt,
                getName()
                );

        return info;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        String passwordBeforeEncrypt = "jiangBUG";
        ByteSource credentialsSalt = ByteSource.Util.bytes("xixixi");
        String salt = credentialsSalt.toString();
        System.out.println("加密盐值为:" + salt);
        SimpleHash simpleHash = new SimpleHash(
                "MD5",
                passwordBeforeEncrypt,
                credentialsSalt,
                2);

        System.out.println(simpleHash.toString());
    }
}
