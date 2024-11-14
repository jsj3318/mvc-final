package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;

import java.util.List;

public interface InquiryRepository {

    // 최근에 작성한 문의가 먼저 보이도록 문의 id 내림차순 정렬

    // 작성자의 아이디로 검색해서 문의 목록 반환
    List<Inquiry> findByUserId(String userId);

    // 답변 완료 되지 않은 문의 목록 반환
    List<Inquiry> findByNoAnswered();

    // 문의 id로 문의 반환
    Inquiry findById(int id);

    // 문의 등록
    Inquiry save(Inquiry inquiry);

}