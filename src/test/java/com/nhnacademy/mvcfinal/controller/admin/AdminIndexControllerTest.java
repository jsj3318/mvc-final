package com.nhnacademy.mvcfinal.controller.admin;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.user.Admin;
import com.nhnacademy.mvcfinal.domain.user.Customer;
import com.nhnacademy.mvcfinal.domain.user.User;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import com.nhnacademy.mvcfinal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = AdminIndexController.class)
class AdminIndexControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private InquiryRepository inquiryRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("어드민이 접속")
    void index_success() throws Exception {
        User admin = new Admin("marco", "4444", "마르코");
        when(userRepository.findById("marco")).thenReturn(admin);

        mockMvc.perform(get("/cs/admin")
                .sessionAttr("userId", "marco"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/adminIndex"));

    }

    @Test
    @DisplayName("고객이 접속")
    void index_fail() throws Exception {
        User customer = new Customer("jsj", "1234", "조승주");
        when(userRepository.findById("jsj")).thenReturn(customer);

        mockMvc.perform(get("/cs/admin")
                        .sessionAttr("userId", "jsj"))
                .andExpect(status().isInternalServerError());

    }

}