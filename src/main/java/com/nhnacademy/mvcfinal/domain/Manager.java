package com.nhnacademy.mvcfinal.domain;

import lombok.AllArgsConstructor;

public class Manager extends User {
    // User를 상속받는 관리자 클래스
    @Override
    String getRole() {
        return "manager";
    }

    public Manager(String id, String password, String name) {
        super(id, password, name);
    }

}
