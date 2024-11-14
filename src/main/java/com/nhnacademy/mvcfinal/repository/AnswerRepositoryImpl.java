package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.answer.Answer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AnswerRepositoryImpl implements AnswerRepository {
    private final Map<Integer, Answer> answerMap;

    public AnswerRepositoryImpl() {
        this.answerMap = new HashMap<>();

        save(new Answer(1, "good", "marco"));
    }

    @Override
    public void save(Answer answer) {
        answerMap.put(answer.getInquiryId(), answer);
    }

    @Override
    public Answer findById(int id) {
        return answerMap.get(id);
    }

}
