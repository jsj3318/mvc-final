package com.nhnacademy.mvcfinal.controller.customer;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.repository.AnswerRepository;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/cs")
@Slf4j
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryRepository inquiryRepository;
    private final AnswerRepository answerRepository;

    @GetMapping("/inquiry/{inquiryId}")
    public String inquiryInfo (
            @PathVariable int inquiryId,
            @SessionAttribute("userId") String userId,
            Model model
    ) throws Exception {
        Inquiry inquiry = inquiryRepository.findById(inquiryId);

        // 자신이 작성하지 않은 문의 페이지에 접근 시도할 시 예외 처리
        if(!inquiry.getUserId().equals(userId)) {
            log.warn("{} 유저가 남의 문의 페이지 접근 시도함!", userId);
            throw new IllegalAccessException("잘못된 접근 시도");
        }

        // model 에 문의 객체 전달
        model.addAttribute("inquiry", inquiry);

        // 문의가 답변 된 거면 답변 객체 가져와서 전달
        if(inquiry.isAnswered()){
            model.addAttribute("answer", answerRepository.findById(inquiryId));
        }

        return "customer/inquiryInfo";
    }

}
