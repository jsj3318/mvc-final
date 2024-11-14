package com.nhnacademy.mvcfinal.repository;

import com.nhnacademy.mvcfinal.domain.Customer;
import com.nhnacademy.mvcfinal.domain.Manager;
import com.nhnacademy.mvcfinal.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> userMap;

    public UserRepositoryImpl() {
        this.userMap = new HashMap<>();

        // 고객과 담당자 여기서 미리 생성하기
        userMap.put("marco", new Manager("marco", "4444", "마르코"));
        userMap.put("jsj", new Customer("jsj", "1234", "조승주"));

    }

    @Override
    public boolean match(String id, String password) {
        return Optional.ofNullable(findById(id))
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public User findById(String id) {
        return userMap.get(id);
    }

}

