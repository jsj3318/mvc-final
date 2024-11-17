package com.nhnacademy.mvcfinal.domain.inquiry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InquiryTest {

    private Inquiry inquiry;

    @BeforeEach
    void setUp() {
        inquiry = new Inquiry("title", "content", "jsj", InquiryCategory.PROBLEM);
    }

    @Test
    void addImage() {
        inquiry.addImage("/test/path1");
        inquiry.addImage("/test/path2");

        assertEquals(2, inquiry.getImages().size());
    }
}