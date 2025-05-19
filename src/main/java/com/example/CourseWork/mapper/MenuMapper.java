package com.example.CourseWork.mapper;

import com.example.CourseWork.dto.DishResponseDto;
import com.example.CourseWork.dto.MenuResponseDto;
import com.example.CourseWork.dto.MenuWithDishesDto;
import com.example.CourseWork.model.Menu;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MenuMapper {
    
    public MenuResponseDto toResponseDto(Menu menu) {
        MenuResponseDto dto = new MenuResponseDto();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        return dto;
    }

    public MenuWithDishesDto toMenuWithDishesDto(Menu menu) {
        MenuWithDishesDto dto = new MenuWithDishesDto();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        dto.setDishes(menu.getDishes().stream().map(dish -> {
            DishResponseDto dishDto = new DishResponseDto();
            dishDto.setId(dish.getId());
            dishDto.setName(dish.getName());
            dishDto.setDescription(dish.getDescription());
            dishDto.setPrice(dish.getPrice());
            dishDto.setIsAvailable(dish.getIsAvailable());
            return dishDto;
        }).collect(Collectors.toList()));
        return dto;
    }
} 