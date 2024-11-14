package com.nhnacademy.mvcfinal.domain.user;

public class Customer extends User {
    // User를 상속받는 고객 클래스
    @Override
    UserRole getRole() {
        return UserRole.CUSTOMER;
    }

    public Customer(String id, String password, String name) {
        super(id, password, name);
    }

}
