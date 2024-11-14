package com.nhnacademy.mvcfinal.domain.inquiry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class InquiryRequest {

    @NotBlank @Size(max = 200, min = 2)
    String title;               // 문의 제목

    @NotBlank @Size(max = 40000)
    String content;             // 문의 내용

    InquiryCategory category;   // 문의 분류 카테고리

}
