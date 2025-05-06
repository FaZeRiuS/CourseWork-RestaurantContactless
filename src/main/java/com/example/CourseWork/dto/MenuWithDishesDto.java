package com.example.CourseWork.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuWithDishesDto {
    private Integer id;
    private String name;
    private List<DishResponseDto> dishes;
}
