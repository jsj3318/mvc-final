package com.nhnacademy.mvcfinal.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class User {
    // 다형성 기반 구현
    // 고객과 관리자를 상속해주기 위한 추상 클래스

    private String id;          // id (marco)
    private String password;    // 암호 (1234)
    private String name;        // 이름 (마르코)

    abstract UserRole getRole();  // 역할 문자열 반환 (customer, manager)

}
