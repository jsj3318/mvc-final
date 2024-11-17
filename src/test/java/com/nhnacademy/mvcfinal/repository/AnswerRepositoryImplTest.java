package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.answer.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnswerRepositoryImplTest {

    private AnswerRepositoryImpl answerRepositoryImpl;

    @BeforeEach
    void setUp() {
        answerRepositoryImpl = new AnswerRepositoryImpl();
    }

    @Test
    void test() {
        // 답변 객체 만들어서 집어넣고 꺼내보는 테스트
        Answer answer1 = new Answer(1, "test", "marco");
        answerRepositoryImpl.save(answer1);

        Answer actual = answerRepositoryImpl.findById(1);
        assertEquals(answer1, actual);
    }
}