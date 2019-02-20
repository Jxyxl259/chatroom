package com.jiang.chatroom.config.shiro;

import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;

import javax.servlet.Filter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName MyFilterChainManager
 * @Description
 * @Author jiangxy
 * @Date 2018\11\14 0014 16:23
 * @Version 1.0.0
 */
public class MyFilterChainManager extends DefaultFilterChainManager {

    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;
    private Map<String, Filter> customFilters =  new LinkedHashMap<>();
    private Map<String, String> filterChainDefinitionMap =  new LinkedHashMap<>();

    public MyFilterChainManager (){
        super();
    }

    public void organizeFilterChainByDefinitionMap(){
        this.initDefaultFilters();
        this.addCustomFilters();
        this.configAllFiltersByFilterChainDefinitionMap();
    }


    // @PostConstruct这种初始化赋值方式只适用于 @Component 或者 ClassPathXmlApplicationContext方式装载的bean
    // 对于 BeanFactory方式装载bean, @PostConstruct这种初始化赋值方式是不生效的
    // 解决办法，实现 InitializingBean 接口，重写 afterPropertiesSet()方法
//    @PostConstruct
//    public void extractChain(){
//
//    }

    /**
     * 初始化 shiro提供的默认过滤器 到过滤器链管理器（当前对象）中
     */
    private void initDefaultFilters (){

        Map<String, Filter> defaultFilters = this.getFilters();
        Iterator _dfItor = defaultFilters.values().iterator();
        while(_dfItor.hasNext()) {
            Filter filter = (Filter)_dfItor.next();
            this.applyGlobalPropertiesIfNecessary(filter);
        }

    }

    /**
     * 将我们自己扩展的过滤器加入到 过滤器链管理器（当前对象）中
     */
    private void addCustomFilters() {
        Map<String, Filter> customFilters = this.getCustomFilters();
        String name;
        Filter filter;
        if (!CollectionUtils.isEmpty(customFilters)) {
            Iterator<Map.Entry<String, Filter>> _cfItor = customFilters.entrySet().iterator();
            while(_cfItor.hasNext()){
                Map.Entry<String, Filter> entry = _cfItor.next();
                name = (String)entry.getKey();
                filter = (Filter)entry.getValue();
                this.applyGlobalPropertiesIfNecessary(filter);
                if (filter instanceof Nameable) {
                    ((Nameable)filter).setName(name);
                }
                this.addFilter(name, filter, false);
            }
        }
    }


    /**
     * 通过 FilterChainDefinitionMap 配置所有的过滤器
     */
    private void configAllFiltersByFilterChainDefinitionMap() {
        Map<String, String> definitionMap = this.getFilterChainDefinitionMap();
        if (!CollectionUtils.isEmpty(definitionMap)) {
            Iterator _defItor = definitionMap.entrySet().iterator();

            while(_defItor.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)_defItor.next();
                String url = (String)entry.getKey();
                String chainDefinition = (String)entry.getValue();
                this.createChain(url, chainDefinition);
            }
        }
    }


    private void applyGlobalPropertiesIfNecessary(Filter filter) {
        this.applyLoginUrlIfNecessary(filter);
        this.applySuccessUrlIfNecessary(filter);
        this.applyUnauthorizedUrlIfNecessary(filter);
    }

    private void applyLoginUrlIfNecessary(Filter filter) {
        String loginUrl = this.getLoginUrl();
        if (StringUtils.hasText(loginUrl) && filter instanceof AccessControlFilter) {
            AccessControlFilter acFilter = (AccessControlFilter)filter;
            String existingLoginUrl = acFilter.getLoginUrl();
            if ("/login.jsp".equals(existingLoginUrl)) {
                acFilter.setLoginUrl(loginUrl);
            }
        }

    }

    private void applySuccessUrlIfNecessary(Filter filter) {
        String successUrl = this.getSuccessUrl();
        if (StringUtils.hasText(successUrl) && filter instanceof AuthenticationFilter) {
            AuthenticationFilter authcFilter = (AuthenticationFilter)filter;
            String existingSuccessUrl = authcFilter.getSuccessUrl();
            if ("/".equals(existingSuccessUrl)) {
                authcFilter.setSuccessUrl(successUrl);
            }
        }

    }

    private void applyUnauthorizedUrlIfNecessary(Filter filter) {
        String unauthorizedUrl = this.getUnauthorizedUrl();
        if (StringUtils.hasText(unauthorizedUrl) && filter instanceof AuthorizationFilter) {
            AuthorizationFilter authzFilter = (AuthorizationFilter)filter;
            String existingUnauthorizedUrl = authzFilter.getUnauthorizedUrl();
            if (existingUnauthorizedUrl == null) {
                authzFilter.setUnauthorizedUrl(unauthorizedUrl);
            }
        }

    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public Map<String, Filter> getCustomFilters() {
        return customFilters;
    }

    public void setCustomFilters(Map<String, Filter> customFilters) {
        this.customFilters = customFilters;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

}
