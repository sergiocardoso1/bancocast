package br.com.cast.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginInterceptorAppConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
            .excludePathPatterns(
                "/login", 
                "/login/**",
                "/error", 
                "/logar", 
                "/img/**",
                "/vendor/**",
                "/js/**", 
                "/favicon.ico", 
                "/css/**",
                "/registrar",
                "/registrar_conta",
                "" 
            );
    }
}
