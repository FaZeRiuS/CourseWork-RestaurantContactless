package com.example.CourseWork.repository;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByOrderByCreatedAtDesc();
    List<Order> findAllByStatusOrderByCreatedAtDesc(OrderStatus status);
}
