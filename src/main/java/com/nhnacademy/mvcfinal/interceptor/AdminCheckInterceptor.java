package com.nhnacademy.mvcfinal.interceptor;

import com.nhnacademy.mvcfinal.domain.user.User;
import com.nhnacademy.mvcfinal.domain.user.UserRole;
import com.nhnacademy.mvcfinal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 세션에서 id를 가져와서 어드민이 맞는지 확인 후, 아니면 예외 던지기
        // 로그인 확인 인터셉터가 먼저 거르므로 세션과 유저 아이디는 존재할 것임
        String userId = (String) request.getSession().getAttribute("userId");
        User user = userRepository.findById(userId);
        if(user.getRole() != UserRole.ADMIN){
            throw new IllegalAccessException("해당 페이지로 접근 할 수 없습니다!");
        }

        return true;
    }


}
