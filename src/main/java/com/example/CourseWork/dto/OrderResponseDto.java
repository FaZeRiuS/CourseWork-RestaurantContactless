package com.example.CourseWork.dto;

import com.example.CourseWork.addition.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Integer id;
    private Integer userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Float totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
}
