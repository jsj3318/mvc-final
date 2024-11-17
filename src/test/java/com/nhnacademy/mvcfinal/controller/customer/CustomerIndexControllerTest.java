package com.nhnacademy.mvcfinal.controller.customer;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import com.nhnacademy.mvcfinal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerIndexController.class)
class CustomerIndexControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InquiryRepository inquiryRepository;
    @MockBean
    // 인터셉터에서 필요하기 때문에 만들어 놓지 않으면 테스트가 실행이 안됨.
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void index() throws Exception {
        List<Inquiry> inquiryList = Arrays.asList(
                new Inquiry("title", "content", "jsj", InquiryCategory.PRAISE)
        );

        when(inquiryRepository.findByUserIdAndCategory("jsj", null))
                .thenReturn(inquiryList);

        mockMvc.perform(get("/cs/")
                .sessionAttr("userId", "jsj"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/customerIndex"))
                .andExpect(model().attribute("inquiryList", inquiryList))
                .andExpect(content().string(containsString("<td>title</td>")));
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/cs/logout")
                        .sessionAttr("userId", "jsj"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/login"));
    }
}