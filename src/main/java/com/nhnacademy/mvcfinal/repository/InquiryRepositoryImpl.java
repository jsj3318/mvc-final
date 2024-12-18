package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InquiryRepositoryImpl implements InquiryRepository {

    private final Map<Integer, Inquiry> inquiryMap;

    public InquiryRepositoryImpl() {
        inquiryMap = new HashMap<>();
    }


    @Override
    public List<Inquiry> findByUserIdAndCategory(String userId, String category) {
        return filterAndSortInquiry(
                // 유저 아이디로 필터링
                inquiryMap.values().stream()
                        .filter(inquiry -> inquiry.getUserId().equals(userId))
                        .toList(),
                category
        );

    }


    @Override
    public List<Inquiry> findByNoAnsweredAndCategory(String category) {
        return filterAndSortInquiry(
                // 답변되지 않은 여부로 필터링
                inquiryMap.values().stream()
                        .filter(inquiry -> !inquiry.isAnswered())
                        .toList(),
                category
        );
    }

    private List<Inquiry> filterAndSortInquiry(List<Inquiry> inquiries, String category) {
        // 카테고리로 필터링 하고 id 역순으로 정렬 -> 최근 등록한 것이 앞으로 온다
        if (category != null && !category.isEmpty()) {
            inquiries = inquiries.stream()
                    .filter(inquiry -> inquiry.getCategory().name().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        } else {
            inquiries = new ArrayList<>(inquiries);
        }

        inquiries.sort((iq1, iq2) -> Integer.compare(iq2.getId(), iq1.getId()));
        return inquiries;
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
