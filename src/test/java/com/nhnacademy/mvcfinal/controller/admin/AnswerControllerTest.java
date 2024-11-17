package com.nhnacademy.mvcfinal.controller.admin;

import com.nhnacademy.mvcfinal.domain.answer.Answer;
import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import com.nhnacademy.mvcfinal.domain.user.Admin;
import com.nhnacademy.mvcfinal.domain.user.Customer;
import com.nhnacademy.mvcfinal.domain.user.User;
import com.nhnacademy.mvcfinal.exception.ValidationFailedException;
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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnswerController.class)
class AnswerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnswerRepository answerRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private InquiryRepository inquiryRepository;

    private User user;
    private Inquiry inquiry;
    private User admin;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = new Customer("jsj", "1234", "조승주");
        when(userRepository.findById("jsj")).thenReturn(user);

        inquiry = new Inquiry("inquiry title", "inquiry content", "jsj", InquiryCategory.SUGGESTION);
        inquiry.setId(1);
        when(inquiryRepository.findById(1)).thenReturn(inquiry);

        admin = new Admin("marco", "4444", "마르코");
        when(userRepository.findById("marco")).thenReturn(admin);

        answer = new Answer(1, "answer content", "marco");


    }

    @Test
    void answerForm() throws Exception {
        mockMvc.perform(get("/cs/admin/answer/1")
                .sessionAttr("userId", "marco"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/answerForm"))
                .andExpect(model().attribute("inquiry", inquiry))
                .andExpect(model().attribute("name", "조승주"));
    }

    @Test
    @DisplayName("이미 답변 완료된 문의의 답변 시도")
    void answerForm_alreadyAnswered() throws Exception {
        // 문의 답변 완료 처리
        inquiry.doAnswer();

        mockMvc.perform(get("/cs/admin/answer/1")
                        .sessionAttr("userId", "marco"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("답변 정상 처리")
    void answer() throws Exception {
        mockMvc.perform(post("/cs/admin/answer/1")
                        .sessionAttr("userId", "marco")
                        .param("content", answer.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));

        verify(answerRepository, times(1)).save(any(Answer.class));
        assertTrue(inquiry.isAnswered());
    }

    @Test
    @DisplayName("답변 처리 실패 - 빈 내용")
    void answer_fail() throws Exception {
        mockMvc.perform(post("/cs/admin/answer/1")
                        .sessionAttr("userId", "marco")
                        .param("content", ""))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertInstanceOf(ValidationFailedException.class, result.getResolvedException()));
    }

}