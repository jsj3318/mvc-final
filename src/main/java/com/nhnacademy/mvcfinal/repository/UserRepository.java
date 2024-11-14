package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.User;

public interface UserRepository {

    // id와 암호가 일치하는지 확인
    boolean match(String id, String password);

    // id로 유저 객체 반환
    User findById(String id);

}
