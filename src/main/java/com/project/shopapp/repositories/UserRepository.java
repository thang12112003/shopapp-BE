package com.project.shopapp.repositories;

import com.project.shopapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);//kiểm tra xem user có phonenumber có tồn tại không
    Optional<User> findByPhoneNumber(String phoneNumber);//trả về user hoặc null
    //SELECT * FROM users WHERE phoneNumber=?
}

