package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
        // 기본으로 등록되어있는 유저
        // customer
        // jsj 1234 조승주
        // hus 1234 한의석
        // admin
        // marco 4444 마르코
    }

    @Test
    void findById() {
        User actual = userRepository.findById("jsj");
        assertEquals("조승주", actual.getName());
    }

    @Test
    void findById_null() {
        User actual = userRepository.findById("wrong");
        assertNull(actual);
    }

    @Test
    void match() {
        assertTrue(userRepository.match("jsj", "1234"));
        assertFalse(userRepository.match("marco", "1"));
    }

}