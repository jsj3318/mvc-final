package com.nhnacademy.mvcfinal.controller.admin;

import com.nhnacademy.mvcfinal.domain.answer.Answer;
import com.nhnacademy.mvcfinal.domain.answer.AnswerRequest;
import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.exception.ValidationFailedException;
import com.nhnacademy.mvcfinal.repository.AnswerRepository;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cs/admin")
@RequiredArgsConstructor
public class AnswerController {
    private final InquiryRepository inquiryRepository;
    private final AnswerRepository answerRepository;

    @GetMapping("/answer/{inquiryId}")
    public String answerForm(
        Model model,
        @PathVariable("inquiryId") int inquiryId
    ) throws IllegalAccessException {
        // 해당 아이디의 문의의 답변 작성 페이지 뷰 출력
        // url 조작으로 왔을 시 이미 답변 완료된 문의일 수 있음
        // 검사해서 예외 발생
        Inquiry inquiry = inquiryRepository.findById(inquiryId);
        if(inquiry.isAnswered()){
            // 이미 답변 완료된 문의다
            throw new IllegalAccessException("이미 답변 완료된 문의입니다!");
        }

        model.addAttribute("inquiry", inquiry);
        return "admin/answerForm";
    }

    @PostMapping("/answer/{inquiryId}")
    public String answer(
            @Validated @ModelAttribute AnswerRequest answerRequest,
            BindingResult bindingResult,
            @PathVariable("inquiryId") int inquiryId,
            @SessionAttribute("userId") String userId
            ) {
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        // 답변을 생성하고 저장한 후, 관리자 메인 페이지로 이동한다
        Answer answer = new Answer(
                inquiryId,
                answerRequest.getContent(),
                userId
        );
        answerRepository.save(answer);

        return "redirect:/cs/admin";
    }

}
