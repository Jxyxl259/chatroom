package com.jiang.chatroom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

//@EnableWebMvc
@Configuration
public class SpringMvcExtendedConfiguration extends WebMvcConfigurerAdapter {


    /**
     * 添加视图解析器
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 主页面
        registry.addViewController("/").setViewName("homepage");
        //registry.addViewController("/homepage.html").setViewName("index");
        //registry.addViewController("/index").setViewName("homepage");

        registry.addViewController("/toLoginPage").setViewName("front/login");
        // 未授权页面
        registry.addViewController("/toUnauthorizedPage").setViewName("unauthorized");
        // 后台页面
        registry.addViewController("/zone/mc").setViewName("zone/zone_mc");
    }

    /**
	 * 配置 locale解析对象
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver(){
		return new LocaleResolver() {
			@Override
			public Locale resolveLocale(HttpServletRequest httpServletRequest) {
				String local = httpServletRequest.getParameter("language");

				if(!StringUtils.isEmpty(local)){
					String[] lang_coun = local.split("_");

					if(Locale.CHINA.getLanguage().equals(lang_coun[0])){
						return new Locale(lang_coun[0], lang_coun[1]);
					}
					if(Locale.US.getLanguage().equals(lang_coun[0])){
						return new Locale(lang_coun[0], lang_coun[1]);
					}
				}

				return  httpServletRequest.getLocale();
			}

			@Override
			public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

			}
		};
	}

}