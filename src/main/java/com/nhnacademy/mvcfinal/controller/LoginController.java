package com.nhnacademy.mvcfinal.controller;

import com.nhnacademy.mvcfinal.exception.LoginFailException;
import com.nhnacademy.mvcfinal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cs/login")
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String loginForm(
        @SessionAttribute(value = "userId", required = false) String id
    ) {
        // 세션에 아이디가 있다 -> 이미 로그인 한 상태 -> 메인 페이지로 연결 시켜 주기
        if(id != null) {
            return redirectMain(id);
        }

        // 세션에 아이디가 없다 -> 로그인 폼 보여주기
        return "loginForm";
    }

    @PostMapping
    public String login(
            @RequestParam("id") String id,
            @RequestParam("pwd") String pwd,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 입력 받은 아이디와 암호가 존재하고 일치하는지 검사한다
        if(userRepository.match(id, pwd)) {
            // 입력된 정보가 존재하며 일치한다
            // 세션을 생성하고 세션 어트리뷰트에 아이디 값을 저장한다
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", id);

            // 메인 페이지로 연결 시켜준다
            return redirectMain(id);
        }

        // id가 존재하지 않거나 일치하지 않음
        // 로그인 실패 예외 발생시킴
        throw new LoginFailException(id+" 계정 로그인 실패!");

    }



    // 유저 아이디를 받고 해당 유저의 역할에 따라 기본 페이지 리다이렉트를 반환하는 함수
    // 해당 유저가 고객일 경우 고객 메인 /cs/
    // 해당 유저가 관리자일 경우 관리자 메인 /cs/admin
    private String redirectMain(String userId){
        switch (userRepository.findById(userId).getRole()){
            case ADMIN:
                return "redirect:/cs/admin";
            case CUSTOMER:
                return "redirect:/cs/";
        }
    }

}
