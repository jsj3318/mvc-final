package com.nhnacademy.mvcfinal.domain.user;

public class Admin extends User {
    // User를 상속받는 관리자 클래스
    @Override
    UserRole getRole() {
        return UserRole.ADMIN;
    }

    public Admin(String id, String password, String name) {
        super(id, password, name);
    }

}
