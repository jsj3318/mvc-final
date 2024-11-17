package com.nhnacademy.mvcfinal.controller.customer;

import com.nhnacademy.mvcfinal.domain.answer.Answer;
import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import com.nhnacademy.mvcfinal.domain.user.Admin;
import com.nhnacademy.mvcfinal.domain.user.User;
import com.nhnacademy.mvcfinal.repository.AnswerRepository;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import com.nhnacademy.mvcfinal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryController.class)
class InquiryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InquiryRepository inquiryRepository;
    @MockBean
    private AnswerRepository answerRepository;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 문의 두개 만들기
        // 문의 1 답변 없음
        Inquiry inquiry1 = new Inquiry("title1", "content1", "jsj", InquiryCategory.PRAISE);
        inquiry1.setId(1);
        when(inquiryRepository.findById(1)).thenReturn(inquiry1);

        // 문의 2 답변 있음
        Inquiry inquiry2 = new Inquiry("title2", "content2", "jsj", InquiryCategory.PRAISE);
        inquiry2.setId(2);
        inquiry2.doAnswer();
        when(inquiryRepository.findById(2)).thenReturn(inquiry2);

        User admin = new Admin("marco", "4444", "마르코");
        when(userRepository.findById("marco")).thenReturn(admin);

        Answer answer = new Answer(2, "answer", "marco");
        when(answerRepository.findById(2)).thenReturn(answer);
    }

    @Test
    @DisplayName("답변 안된 문의 조회")
    void inquiryInfo() throws Exception {
        mockMvc.perform(get("/cs/inquiry/1")
                .sessionAttr("userId", "jsj"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/inquiryInfo"))
                .andExpect(model().attributeExists("inquiry"))
                .andExpect(model().attributeDoesNotExist("answer"))
                .andExpect(model().attributeDoesNotExist("name"))
                .andExpect(content().string(containsString("답변 없음")));
    }

    @Test
    @DisplayName("답변 된 문의 조회")
    void inquiryInfo_answered() throws Exception {
        mockMvc.perform(get("/cs/inquiry/2")
                        .sessionAttr("userId", "jsj"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/inquiryInfo"))
                .andExpect(model().attributeExists("inquiry"))
                .andExpect(model().attributeExists("answer"))
                .andExpect(model().attributeExists("name"))
                .andExpect(content().string(containsString("마르코 (marco)")));
    }

    @Test
    @DisplayName("남의 문의에 접속 시도")
    void inquiryInfo_wrongAccess() throws Exception {
        mockMvc.perform(get("/cs/inquiry/1")
                        .sessionAttr("userId", "hus"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertInstanceOf(IllegalAccessException.class, result.getResolvedException()));
    }

}