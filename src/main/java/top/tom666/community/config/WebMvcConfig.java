package top.tom666.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.tom666.community.controller.interceptor.AlphaInterceptor;
import top.tom666.community.controller.interceptor.LoginTicketInterceptor;

/**
 * @author: liujisen
 * @date： 2022-08-28
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(alphaInterceptor).excludePathPatterns("/**/*.css","/**/*.js").
//        addPathPatterns("/register","/login");
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");    }
}
