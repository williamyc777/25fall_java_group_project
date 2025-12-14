//package org.example.backend.interceptor;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AbstractUserInterceptor())
//                .addPathPatterns("/**")                     // 注册JWT拦截器
//                .excludePathPatterns("/login/**", "/signUp/**", "/error", "/websocket");               // 不拦截的请求
//        registry.addInterceptor(new UserInterceptor())   // 注册JWT拦截器
//                .addPathPatterns("/signUp/**","/comment/**", "/event/**","/image/**",)                    // 要拦截的请求
//                .excludePathPatterns("/login/**", "/error");               // 不拦截的请求
//        registry.addInterceptor(new AdminInterceptor())   // 注册JWT拦截器
//                .addPathPatterns("/teacher/**")                    // 要拦截的请求
//                .excludePathPatterns("/login/**", "/error");               // 不拦截的请求
//    }
//}
