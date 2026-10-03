package br.edu.ifpb.spendwise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AcessoInterceptor acessoInterceptor;

    public WebConfig(AcessoInterceptor acessoInterceptor) {
        this.acessoInterceptor = acessoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(acessoInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/login", "/css/**", "/error", "/favicon.ico");
    }
}
