package com.example.CourseWork.service;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.OrderRequestDto;
import com.example.CourseWork.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(String userId, OrderRequestDto dto);
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto getOrderById(Integer id);
    List<OrderResponseDto> getNewOrders();
    OrderResponseDto confirmOrderFromCart(String userId);
    OrderResponseDto updateOrderStatus(Integer id, OrderStatus newStatus);
}
