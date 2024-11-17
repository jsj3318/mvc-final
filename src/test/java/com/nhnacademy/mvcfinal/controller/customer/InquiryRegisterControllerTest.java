package com.nhnacademy.mvcfinal.controller.customer;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import com.nhnacademy.mvcfinal.exception.ValidationFailedException;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import com.nhnacademy.mvcfinal.repository.UserRepository;
import com.nhnacademy.mvcfinal.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryRegisterController.class)
class InquiryRegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InquiryRepository inquiryRepository;
    @MockBean
    private FileService fileService;
    @MockBean
    private UserRepository userRepository;

    @Value("${imagesPath}")
    private String imagesPath;

    @BeforeEach
    void setUp() {

    }

    @Test
    void registerForm() throws Exception {
        mockMvc.perform(get("/cs/inquiry/register")
                .sessionAttr("userId", "jsj"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/inquiryForm"));
    }

    @Test
    @DisplayName("문의 등록 성공")
    void register() throws Exception {
        int inquiryId = 1;
        String title = "title";
        String content = "content";

        Inquiry inquiry = new Inquiry(title, content, "jsj", InquiryCategory.PROBLEM);
        inquiry.setId(inquiryId);
        given(inquiryRepository.save(any(Inquiry.class))).willReturn(inquiry);

        mockMvc.perform(post("/cs/inquiry/register")
                .sessionAttr("userId", "jsj")
                .param("title", title)
                .param("content", content)
                .param("category", InquiryCategory.PROBLEM.name())
                .param("images", "")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/inquiry/1"));

        verify(inquiryRepository, times(1)).save(any(Inquiry.class));

    }


    @Test
    @DisplayName("이미지있는 문의 등록 성공")
    void register_images() throws Exception {
        int inquiryId = 1;
        String title = "title";
        String content = "content";

        Inquiry inquiry = new Inquiry(title, content, "jsj", InquiryCategory.PROBLEM);
        inquiry.setId(inquiryId);
        given(inquiryRepository.save(any(Inquiry.class))).willReturn(inquiry);

        // 이미지 업로드 방지하기
        doNothing().when(fileService).createDir(any(Path.class));
        doNothing().when(fileService).uploadInquiryImages(anyString(), anyList(), any(Inquiry.class));

        MockMultipartFile imageFile = new MockMultipartFile("images", "test.jpg", "image/jpg", new byte[]{1, 2, 3});
        MockMultipartFile imageFile2 = new MockMultipartFile("images", "test.png", "image/png", new byte[]{1, 2, 3});
        given(fileService.cleanEmptyFiles(anyList())).willReturn(Arrays.asList(imageFile, imageFile2));

        mockMvc.perform(multipart("/cs/inquiry/register")
                        .file(imageFile)
                        .file(imageFile2)
                        .sessionAttr("userId", "jsj")
                        .param("title", title)
                        .param("content", content)
                        .param("category", InquiryCategory.PROBLEM.name())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/inquiry/1"));

        verify(inquiryRepository, times(1)).save(any(Inquiry.class));
        verify(fileService, times(1)).uploadInquiryImages(anyString(), anyList(), any(Inquiry.class));

    }

    @Test
    @DisplayName("문의 등록 실패 - 잘못된 입력")
    void register_fail() throws Exception {
        int inquiryId = 1;
        String title = "";  // 빈 제목
        String content = "content";

        Inquiry inquiry = new Inquiry(title, content, "jsj", InquiryCategory.PROBLEM);
        inquiry.setId(inquiryId);
        given(inquiryRepository.save(any(Inquiry.class))).willReturn(inquiry);

        mockMvc.perform(post("/cs/inquiry/register")
                        .sessionAttr("userId", "jsj")
                        .param("title", title)
                        .param("content", content)
                        .param("category", InquiryCategory.PROBLEM.name())
                        .param("images", "")
                )
                        .andExpect(result ->
                                assertInstanceOf(ValidationFailedException.class, result.getResolvedException()));

    }

}