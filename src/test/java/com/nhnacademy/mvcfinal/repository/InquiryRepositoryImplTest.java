package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class InquiryRepositoryImplTest {

    private InquiryRepository inquiryRepository;

    @BeforeEach
    void setUp() {
        inquiryRepository = new InquiryRepositoryImpl();

        inquiryRepository.save( new Inquiry("title1", "content1", "jsj", InquiryCategory.PROBLEM));
        inquiryRepository.save( new Inquiry("title2", "content2", "jsj", InquiryCategory.PRAISE));
        inquiryRepository.save( new Inquiry("title3", "content3", "hus", InquiryCategory.SUGGESTION));
        inquiryRepository.save( new Inquiry("title4", "content4", "hus", InquiryCategory.PROBLEM));
        inquiryRepository.save( new Inquiry("title5", "content5", "jsj", InquiryCategory.PRAISE));
        inquiryRepository.save( new Inquiry("title6", "content6", "hus", InquiryCategory.PRAISE));

        inquiryRepository.findById(2).doAnswer();
        inquiryRepository.findById(4).doAnswer();
    }

    @Test
    void findByUserIdAndCategory() {
        List<Inquiry> actual = inquiryRepository.findByUserIdAndCategory("jsj", "PRAISE");
        assertEquals(2, actual.size());
        assertEquals(5, actual.get(0).getId());
        assertEquals(2, actual.get(1).getId());
    }

    @Test
    void findByNoAnsweredAndCategory() {
        List<Inquiry> actual = inquiryRepository.findByNoAnsweredAndCategory("PRAISE");
        assertEquals(2, actual.size());
        assertEquals(6, actual.get(0).getId());
        assertEquals(5, actual.get(1).getId());
    }

    @Test
    void findById() {
        Inquiry actual = inquiryRepository.findById(2);
        assertEquals("title2", actual.getTitle());
    }
}