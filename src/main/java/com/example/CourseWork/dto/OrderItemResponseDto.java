package com.example.CourseWork.dto;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Integer dishId;
    private String dishName;
    private Integer quantity;
    private String specialRequest;
}
