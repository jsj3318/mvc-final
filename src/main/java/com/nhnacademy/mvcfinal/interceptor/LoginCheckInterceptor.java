package com.nhnacademy.mvcfinal.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 세션에서 id를 가져와보고 존재하지 않으면 login으로 리다이렉트 시킨다

        HttpSession session = request.getSession(false);
        String id = null;
        if (session != null) {
            id = (String) session.getAttribute("userId");
        }

        if(id == null){
            response.sendRedirect("/cs/login");
            return false;
        }

        return true;
    }

}
