package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;

import java.util.List;

public interface InquiryRepository {

    // 최근에 작성한 문의가 먼저 보이도록 문의 id 내림차순 정렬

    // 아이디와 카테고리로 목록 반환
    List<Inquiry> findByUserIdAndCategory(String userId, String category);

    // 답변 완료 되지 않은 문의 목록의 카테고리 필터링
    List<Inquiry> findByNoAnsweredAndCategory(String category);

    // 문의 id로 문의 반환
    Inquiry findById(int id);

    // 문의 등록
    Inquiry save(Inquiry inquiry);

}
