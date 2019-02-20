package com.jiang.chatroom.config.shiro;

import com.jiang.chatroom.config.SpringCacheManagerWrapper;
import com.jiang.chatroom.config.SpringConfig;
import com.jiang.chatroom.service.UserService;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: Shiro权限管理框架配置
 * @author: jiangxy
 * @create: 2018-07-08 14:17
 */
@Configuration
@Order(3)
public class MyShiroConfig{

    @Autowired
    private SecurityManager securityManager;

    //private ShiroFilterFactoryBean shiroFilterFactory = null;

    //private AbstractShiroFilter abstractShiroFilter = null;

    private org.springframework.cache.ehcache.EhCacheCacheManager springCacheCacheManager = null;

    /**
     * SpringCachemanagerWrapper作为缓存包装类,
     * 实现了 org.apache.shiro.cache.CacheManager接口
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager springCacheManagerWrapper(/*org.springframework.cache.ehcache.EhCacheCacheManager springCacheCacheManager*/){

        if(this.springCacheCacheManager == null) {
            SpringConfig springConfig = new SpringConfig();
            this.springCacheCacheManager = springConfig.springCacheCacheManager();
        }
        SpringCacheManagerWrapper springCacheWrapper = new SpringCacheManagerWrapper();
        springCacheWrapper.setCacheManager(springCacheCacheManager);
        return springCacheWrapper;
    }


    /**
     * 创建凭证匹配器
     * 密码尝试次数限制为5次,由缓存实现
     * @return
     */
    @Bean
    @Autowired
    @ConditionalOnMissingBean(CredentialsMatcher.class)
    public CredentialsMatcher hashedCredentialsMatcher(CacheManager CacheManager){
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(CacheManager);
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }


    /**
     * 自定义Realm实现类
     * @return
     */
    @Bean
    @Autowired
    @ConditionalOnBean(UserService.class)
    @ConditionalOnMissingBean(Realm.class)
    public Realm myShiroRealm(CredentialsMatcher credentialsMatcher, UserService userservice){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setUserService(userservice);
        myShiroRealm.setCredentialsMatcher(credentialsMatcher);
        myShiroRealm.setCachingEnabled(true);
        myShiroRealm.setAuthenticationCachingEnabled(true);
        myShiroRealm.setAuthorizationCachingEnabled(true);
        myShiroRealm.setAuthenticationCacheName("authenticationCache");
        myShiroRealm.setAuthorizationCacheName("authorizationCache");
        return myShiroRealm;
    }


    /**
     * 会话Cookie模板
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);

        // cookie 过期时间 设置为永不过期
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }


    /**
     * 记住我功能 cookie
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);

        // cookie 过期时间 设置为30天
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }


    /**
     * rememberMe管理器(为rememberMe COOKIE进行加密)
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(CookieRememberMeManager.class)
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(org.apache.shiro.codec.Base64.decode("fgchain"));
        rememberMeManager.setCookie(rememberMeCookie());
        return rememberMeManager;
    }


    /**
     * 设置会话管理器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DefaultWebSessionManager.class)
    public DefaultWebSessionManager defaultWebSessionManager(){
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setGlobalSessionTimeout(1800000);
//        sessionManager.setDeleteInvalidSessions(true);
//        sessionManager.setSessionIdCookieEnabled(true);
//        sessionManager.setSessionIdCookieEnabled(true);
//        sessionManager.setSessionIdCookie(sessionIdCookie());
        MyShiroSessionManager sessionManager = new MyShiroSessionManager();

        return sessionManager;
    }


    /**
     * 设置安全管理器
     * @param myShiroRealm
     * @param springCacheManagerWrapper
     * @param defaultWebSessionManager
     * @param cookieRememberMeManager
     * @return
     */
    @Bean
    @Autowired
    public SecurityManager securityManager(Realm myShiroRealm,
                                           CacheManager springCacheManagerWrapper,
                                           SessionManager defaultWebSessionManager,
                                           RememberMeManager cookieRememberMeManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        securityManager.setCacheManager(springCacheManagerWrapper);
        securityManager.setSessionManager(defaultWebSessionManager);
        securityManager.setRememberMeManager(cookieRememberMeManager);
        return securityManager;
    }


    /**
     * Shiro生命周期处理器(注解需要)
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        LifecycleBeanPostProcessor processor = new LifecycleBeanPostProcessor();
        return processor;
    }


    @Bean
    public MyPathMatchingFilterChainResolver filterChainResolver(){
        MyPathMatchingFilterChainResolver filterChainResolver = new MyPathMatchingFilterChainResolver();
        MyFilterChainManager filterChainManager = new MyFilterChainManager();

        filterChainManager.setLoginUrl("/toLoginPage");
        filterChainManager.setSuccessUrl("/");
        filterChainManager.setUnauthorizedUrl("/toUnauthorizedPage");

        // 配置过滤器过滤规则
        Map<String, String> filterDefMap = new LinkedHashMap<>();
        filterDefMap.put("/logout", "logout");
        filterDefMap.put("/zone/**", "authc");
        filterChainManager.setFilterChainDefinitionMap(filterDefMap);

        // 此处添加自定义拦截器实现扩展
        Map<String, Filter> customFilters = new LinkedHashMap<>();

        filterChainManager.setCustomFilters(customFilters);

        filterChainManager.organizeFilterChainByDefinitionMap();

        // 往过滤链解析器中设置过滤链管理器
        filterChainResolver.setFilterChainManager(filterChainManager);
        return filterChainResolver;
    }


    /**
     * 代替了下面注释掉的方法，配置 Shiro 其实最终就是往容器里面加了 SpringShiroFilter这个类的一个对象
     * 关于 MethodInvokingFactoryBean 这个类的用法，还不是很清楚，但是下面注释掉的方法先执行的是 MethodInvokingFactoryBean
     * 后执行的 shiroFilterFactoryBean
     * @param filterChainResolver
     * @param securityManager
     * @return
     * @throws Throwable
     */
    @Bean
    @Autowired
    public AbstractShiroFilter SpringShiroFilter(FilterChainResolver filterChainResolver, SecurityManager securityManager) throws Throwable{
        // 创建一个shiro过滤器工厂类
        ShiroFilterFactoryBean shiroFilterFactory = new ShiroFilterFactoryBean();
        shiroFilterFactory.setSecurityManager(new DefaultWebSecurityManager(new MyShiroRealm()));

        AbstractShiroFilter abstractShiroFilter = (AbstractShiroFilter)shiroFilterFactory.getObject();

        // 设置 securityManager
        abstractShiroFilter.setSecurityManager((WebSecurityManager) securityManager);
        // 设置 filterChainResolver
        abstractShiroFilter.setFilterChainResolver(filterChainResolver);

        return abstractShiroFilter;
    }


//    /**
//     * 重新设置 SecurityManager 对象
//     * @return
//     */
//    @Bean
//    @ConditionalOnBean(SecurityManager.class)
//    @Autowired
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
//        // 设置登陆页面,登陆成功页面,未授权页面
//        // 统统将这些配置（SpringShiroFilter的配置(securityManager、FilterChainResolver)）
//        // 全部移到 filterChainManager(FilterChainResolver的属性) 之中了
//
//        // shiroFilterFactory.setLoginUrl("/toLoginPage");
//        // shiroFilterFactory.setSuccessUrl("/index");
//        // shiroFilterFactory.setUnauthorizedUrl("/toUnauthorizedPage");
//        this.abstractShiroFilter.setSecurityManager((WebSecurityManager) securityManager);
//
//        return this.shiroFilterFactory;
//    }



    // 先执行此方法，再执行上面的 shiroFilterFactoryBean(); 方法
//    @Bean
//    public MethodInvokingFactoryBean methodInvokingFactoryBean(FilterChainResolver filterChainResolver) throws Throwable{
//        // 创建一个shiro过滤器工厂类
//        this.shiroFilterFactory = new ShiroFilterFactoryBean();
//        // 随便先设置一个SecurityManager, 之后本类中 shiroFilterFactoryBean();方法会重新设置该对象
//        this.shiroFilterFactory.setSecurityManager(new DefaultWebSecurityManager(new MyShiroRealm()));
//
//        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
//
//        // 在调用shiroFilterFactory.getObject()方法时设置到 SpringShiroFilter中的是默认的 chainManager 和 chainResolver
//        // 之后又通过反射将 chainResolver 替换成我们自定义的 MyPathMatchingFilterChainResolver
//        abstractShiroFilter = (AbstractShiroFilter)shiroFilterFactory.getObject();
//        methodInvokingFactoryBean.setTargetObject(abstractShiroFilter);
//        methodInvokingFactoryBean.setTargetMethod("setFilterChainResolver");
//        methodInvokingFactoryBean.setArguments(filterChainResolver);
//        return methodInvokingFactoryBean;
//    }

}
