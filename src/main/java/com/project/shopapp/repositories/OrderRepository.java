package com.project.shopapp.repositories;

import com.project.shopapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    //Tìm các đơn hàng của 1 user nào đó
    // định nghĩa một phương thức trong OrderRepository để tìm kiếm tất cả các đơn hàng (Order) liên quan đến một người dùng cụ thể (User) dựa trên userId.
    List<Order> findByUserId(Long userId);
}
