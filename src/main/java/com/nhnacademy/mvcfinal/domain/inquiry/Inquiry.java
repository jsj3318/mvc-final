package com.nhnacademy.mvcfinal.domain.inquiry;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Inquiry {
    // 고객이 작성하는 문의 클래스
    @Setter
    private int id;                     // 문의 인덱스 번호
    private String title;               // 문의 제목
    private String content;             // 문의 내용
    private String userId;              // 문의 작성자 아이디
    private boolean answered;           // 문의 답변됨 여부
    private LocalDateTime createdAt;    // 문의 작셩 일시
    private InquiryCategory category;   // 문의 분류 카테고리

    private List<String> images;

    public Inquiry(String title, String content, String userId, InquiryCategory category) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.category = category;

        answered = false;
        createdAt = LocalDateTime.now();
        images = new ArrayList<>();
    }

    public void doAnswer() {
        answered = true;
    }


    public void addImage(Path filePath) {
        images.add(filePath.toString());
    }
}
