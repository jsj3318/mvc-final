package com.nhnacademy.mvcfinal.controller.customer;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryRequest;
import com.nhnacademy.mvcfinal.exception.ValidationFailedException;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cs/inquiry/register")
public class InquiryRegisterController {

    private final InquiryRepository inquiryRepository;

    public InquiryRegisterController(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @GetMapping
    public String registerForm() {
        return "customer/inquiryForm";
    }

    @PostMapping
    public String register(
            @SessionAttribute("userId") String userId,
            @Validated @ModelAttribute InquiryRequest inquiryRequest,
            BindingResult bindingResult
            ) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        Inquiry inquiry = inquiryRepository.save(new Inquiry(
                inquiryRequest.getTitle(),
                inquiryRequest.getContent(),
                userId,
                inquiryRequest.getCategory()
        ));

        return "redirect:/cs/inquiry/" + inquiry.getId();
    }

}
