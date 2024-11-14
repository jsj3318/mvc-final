package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.answer.Answer;

public interface AnswerRepository {

    void save(Answer answer);

    Answer findById(int id);

}
