package com.nhnacademy.mvcfinal.controller.customer;

import com.nhnacademy.mvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.mvcfinal.domain.inquiry.InquiryRequest;
import com.nhnacademy.mvcfinal.exception.ValidationFailedException;
import com.nhnacademy.mvcfinal.repository.InquiryRepository;
import com.nhnacademy.mvcfinal.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/cs/inquiry/register")
@RequiredArgsConstructor
public class InquiryRegisterController {

    private final InquiryRepository inquiryRepository;
    private final FileService fileService;

    @Value("${imagesPath}")
    // 프로퍼티에 지정된 이미지 파일 저장 경로
    private String imagesPath;

    @GetMapping
    public String registerForm() {
        return "customer/inquiryForm";
    }

    @PostMapping
    public String register(
            @SessionAttribute("userId") String userId,
            @Validated @ModelAttribute InquiryRequest inquiryRequest,
            BindingResult bindingResult,
            @RequestParam(value = "images", required = false)List<MultipartFile> images
            ) throws IOException {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        // 문의를 생성하고 저장 후, 해당 문의 상세 페이지로 이동한다
        Inquiry inquiry = inquiryRepository.save(new Inquiry(
                inquiryRequest.getTitle(),
                inquiryRequest.getContent(),
                userId,
                inquiryRequest.getCategory()
        ));

        // 빈 이미지 파일들 필터링하기
        images = fileService.cleanEmptyFiles(images);

        // 경로에 관한 변수 생성
        String path = imagesPath + inquiry.getId() + "/";
        Path dirPath = Paths.get(path);

        // 이미지 파일이 존재한다면 파일 처리
        if(!images.isEmpty()) {

            // 디렉토리 생성
            fileService.createDir(dirPath);

            // 문의에 대해 이미지들 업로드
            fileService.uploadInquiryImages(path, images, inquiry);

            log.info("{}",inquiry.getImages());
        }

        return "redirect:/cs/inquiry/" + inquiry.getId();
    }

}
