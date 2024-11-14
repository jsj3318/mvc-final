package com.nhnacademy.mvcfinal.domain;

import lombok.AllArgsConstructor;

public class Customer extends User {
    // User를 상속받는 고객 클래스
    @Override
    String getRole() {
        return "customer";
    }

    public Customer(String id, String password, String name) {
        super(id, password, name);
    }

}
