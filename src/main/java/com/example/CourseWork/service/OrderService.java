package com.example.CourseWork.service;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.OrderRequestDto;
import com.example.CourseWork.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Integer userId, OrderRequestDto dto);
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto getOrderById(Integer id);
    List<OrderResponseDto> getNewOrders();
    OrderResponseDto confirmOrderFromCart(Integer userId);
    OrderResponseDto updateOrderStatus(Integer Id, OrderStatus status);
}
