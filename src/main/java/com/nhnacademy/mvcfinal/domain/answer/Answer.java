package com.nhnacademy.mvcfinal.domain.answer;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Answer {

    @Setter
    private int inquiryId;

    private String content;

    private LocalDateTime createdAt;

    private String adminId;

    public Answer(int inquiryId, String content, String adminId) {
        this.inquiryId = inquiryId;
        this.content = content;
        this.adminId = adminId;

        this.createdAt = LocalDateTime.now();
    }

}
