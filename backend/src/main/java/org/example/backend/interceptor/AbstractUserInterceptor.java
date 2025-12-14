package org.example.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.domain.AbstractUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.example.backend.util.JwtUtil;
import org.example.backend.config.MyException;

public class AbstractUserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null) {
            System.out.println(request.getRequestURL());
            System.out.println("token is null");
            throw new MyException(1, "token is null");
        }
        AbstractUser abstractUser = JwtUtil.verifyToken(token);
        if (abstractUser == null) {
            System.out.println(request.getRequestURL());
            System.out.println("token is invalid");
            throw new MyException(2, "token is invalid");
        }
        return true;
    }
}
