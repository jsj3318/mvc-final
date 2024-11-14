package com.nhnacademy.mvcfinal.domain.inquiry;

import lombok.Getter;

public enum InquiryCategory {
    PROBLEM("불만 접수"),
    SUGGESTION("제안"),
    REFUND_EXCHANGE("환불/교환"),
    PRAISE("칭찬해요"),
    OTHER("기타 문의");

    @Getter
    private final String name;

    InquiryCategory(String name) {
        this.name = name;
    }

}
