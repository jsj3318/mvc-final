package com.nhnacademy.mvcfinal.controller.customer;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryCategory;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            Model model,
            @RequestParam(value = "category", required = false) String category
    ) {
        // 로그인 되어있는 유저의 아이디를 이용해서
        // 해당 유저가 작성한 문의의 목록을 출력하는 인덱스 페이지 출력

        // 모델로 카테고리 리스트 넘겨주기
        model.addAttribute("categoryList", InquiryCategory.values());
        model.addAttribute("selectedCategory", category);

        // 모델로 문의 리스트를 넘겨주기
        List<Inquiry> inquiryList =  inquiryRepository.findByUserIdAndCategory(userId, category);
        model.addAttribute("inquiryList", inquiryList);
        return "customer/customerIndex";
    }

    @PostMapping("/logout")
    public String logout(
        HttpServletRequest request
    ) {
        // 로그아웃 -> 세션 삭제
        HttpSession session = request.getSession(false);
        session.invalidate();

        return "redirect:/cs/login";
    }

}
