package com.example.CourseWork.mapper;

import com.example.CourseWork.dto.DishResponseDto;
import com.example.CourseWork.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {
    
    public DishResponseDto toResponseDto(Dish dish) {
        DishResponseDto dto = new DishResponseDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setIsAvailable(dish.getIsAvailable());
        dto.setMenuId(dish.getMenu().getId());
        return dto;
    }
} 