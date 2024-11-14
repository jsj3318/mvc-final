package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InquiryRepositoryImpl implements InquiryRepository {

    private final Map<Integer, Inquiry> inquiryMap;

    public InquiryRepositoryImpl() {
        inquiryMap = new HashMap<>();
    }

    @Override
    public List<Inquiry> findByUserId(String userId) {
        List<Inquiry> inquiryList = inquiryMap.values().stream()
                .filter(inquiry -> inquiry.getUserId().equals(userId))
                .toList();

        inquiryList.sort(
                (iq1, iq2) -> Integer.compare(iq2.getId(), iq1.getId())
        );

        return inquiryList;
    }

    @Override
    public List<Inquiry> findByNoAnswered() {
        List<Inquiry> inquiryList = inquiryMap.values().stream()
                .filter(inquiry -> !inquiry.isAnswered())
                .toList();

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
        inquiryMap.put(id, inquiry);
        return inquiry;
    }

}
