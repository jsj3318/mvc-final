package com.nhnacademy.mvcfinal.controller;

import com.nhnacademy.mvcfinal.domain.user.Admin;
import com.nhnacademy.mvcfinal.domain.user.Customer;
import com.nhnacademy.mvcfinal.domain.user.User;
import com.nhnacademy.mvcfinal.exception.LoginFailException;
import com.nhnacademy.mvcfinal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@ContextConfiguration(classes = {UserRepository.class})
class LoginControllerTest {

    private MockMvc mockMvc;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(userRepository))
                .build();

        // 테스트용 유저 생성
        User customer = new Customer("jsj", "1234", "조승주");
        User admin = new Admin("marco", "4444", "마르코");

        when(userRepository.match("jsj", "1234")).thenReturn(true);
        when(userRepository.match("marco", "4444")).thenReturn(true);
        when(userRepository.findById("jsj")).thenReturn(customer);
        when(userRepository.findById("marco")).thenReturn(admin);
    }

    @Test
    @DisplayName("/cs/login 최초로 접속 시")
    void loginForm() throws Exception {
        mockMvc.perform(get("/cs/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }

    @Test
    @DisplayName("세션에 아이디 고객 일 때")
    void loginForm_alreadyLogin() throws Exception {
        mockMvc.perform(get("/cs/login")
                        .sessionAttr("userId", "jsj"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/"));
    }

    @Test
    @DisplayName("세션에 아이디가 관리자일 때")
    void loginForm_alreadyLogin_admin() throws Exception {
        mockMvc.perform(get("/cs/login")
                        .sessionAttr("userId", "marco"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));
    }

    @Test
    @DisplayName("로그인 실패 시")
    void login_fail() throws Exception {
        when(userRepository.match("jsj", "wrong")).thenReturn(false);

        mockMvc.perform(post("/cs/login")
                .param("id", "jsj")
                .param("pwd", "wrong"))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof LoginFailException));

    }


    @Test
    @DisplayName("고객 로그인 성공 시")
    void login_success() throws Exception {
        mockMvc.perform(post("/cs/login")
                        .param("id", "jsj")
                        .param("pwd", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/"));
    }



}