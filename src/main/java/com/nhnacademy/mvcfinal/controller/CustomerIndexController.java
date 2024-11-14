package com.nhnacademy.mvcfinal.controller;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/cs")
public class CustomerIndexController {

    private final InquiryRepository inquiryRepository;

    public CustomerIndexController(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @GetMapping("/")
    public String index(
            @SessionAttribute("userId") String userId,
            Model model
    ) {
        // 로그인 되어있는 유저의 아이디를 이용해서
        // 해당 유저가 작성한 문의의 목록을 출력하는 인덱스 페이지 출력
        // 모델로 리스트를 넘겨주기
        List<Inquiry> inquiryList =  inquiryRepository.findByUserId(userId);
        model.addAttribute("inquiryList", inquiryList);
        return "customer/index";
    }

}
