package com.example.CourseWork.dto;

import com.example.CourseWork.addition.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Integer id;
    private String userId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private float totalPrice;
    private List<OrderItemResponseDto> items;
}
