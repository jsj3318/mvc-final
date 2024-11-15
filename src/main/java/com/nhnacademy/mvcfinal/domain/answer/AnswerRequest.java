package com.nhnacademy.mvcfinal.domain.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AnswerRequest {

    @NotBlank @Size(min = 1, max = 40000)
    String content;


}
