package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InquiryRepositoryImpl implements InquiryRepository {

    private final Map<Integer, Inquiry> inquiryMap;

    public InquiryRepositoryImpl() {
        inquiryMap = new HashMap<>();

        // 테스트용 미리 넣는 문의
        Inquiry inquiry1 = new Inquiry("테스트 제목", "테스트 본문", "jsj", InquiryCategory.PROBLEM);
        inquiry1.doAnswer();
        save(inquiry1);
        save(new Inquiry("테스트 제목2", "테스트 본문2", "jsj", InquiryCategory.PRAISE));
        save(new Inquiry("테스트 제목3", "테스트 본문3", "hus", InquiryCategory.PRAISE));

    }

    @Override
    public List<Inquiry> findByUserId(String userId) {
        List<Inquiry> inquiryList = inquiryMap.values().stream()
                .filter(inquiry -> inquiry.getUserId().equals(userId))
                .collect(Collectors.toList());

        inquiryList.sort(
                (iq1, iq2) -> Integer.compare(iq2.getId(), iq1.getId())
        );

        return inquiryList;
    }

    @Override
    public List<Inquiry> findByNoAnswered() {
        List<Inquiry> inquiryList = inquiryMap.values().stream()
                .filter(inquiry -> !inquiry.isAnswered())
                .collect(Collectors.toList());

        inquiryList.sort(
                (iq1, iq2) -> Integer.compare(iq2.getId(), iq1.getId())
        );

        return inquiryList;
    }

    @Override
    public Inquiry findById(int id) {
        return inquiryMap.get(id);
    }

    @Override
    public Inquiry save(Inquiry inquiry) {
        // inquiry 는 id가 null임
        // 저장 할 때 id를 생성하고 set 해줘야함
        int id = inquiryMap.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
        inquiry.setId(id + 1);
        inquiryMap.put(inquiry.getId(), inquiry);
        return inquiry;
    }

}
