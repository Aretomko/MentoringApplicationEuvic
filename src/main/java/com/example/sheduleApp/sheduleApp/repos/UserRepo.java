package com.example.sheduleApp.sheduleApp.repos;

import com.example.sheduleApp.sheduleApp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername (String username);

    User findByActivationCode(String code);
}
