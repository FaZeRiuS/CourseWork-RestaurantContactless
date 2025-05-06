package com.example.CourseWork.service;

import com.example.CourseWork.dto.DishDto;
import com.example.CourseWork.dto.DishResponseDto;

import java.util.List;

public interface DishService {
    DishResponseDto createDish(DishDto dto);
    DishResponseDto updateDish(Integer id, DishDto dto);
    void deleteDish(Integer id);
    List<DishResponseDto> getAllAvailableDishes();
    DishResponseDto getDishById(Integer id);
}
