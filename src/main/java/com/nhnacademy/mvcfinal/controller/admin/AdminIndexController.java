package com.nhnacademy.mvcfinal.controller.admin;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cs")
public class AdminIndexController {
    private final InquiryRepository inquiryRepository;

    public AdminIndexController(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }


    @GetMapping("/admin")
    public String index(
            Model model,
            @RequestParam(value = "category", required = false) String category
    ) {
        // 어드민이 로그인 했을 때 들어오는 get 요청

        // 카테고리 관련 전달
        model.addAttribute("categoryList", InquiryCategory.values());
        model.addAttribute("selectedCategory", category);

        // 아직 답변 되지 않은 모든 문의를 보여준다
        List<Inquiry> inquiryList = inquiryRepository.findByNoAnsweredAndCategory(category);
        model.addAttribute("inquiryList", inquiryList);

        return "admin/adminIndex";
    }



}
