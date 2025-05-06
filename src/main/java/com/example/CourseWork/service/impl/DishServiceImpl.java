package com.example.CourseWork.service.impl;

import com.example.CourseWork.dto.DishDto;
import com.example.CourseWork.dto.DishResponseDto;
import com.example.CourseWork.model.Dish;
import com.example.CourseWork.model.Menu;
import com.example.CourseWork.repository.DishRepository;
import com.example.CourseWork.repository.MenuRepository;
import com.example.CourseWork.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;

    @Override
    public DishResponseDto createDish(DishDto dto) {
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setIsAvailable(dto.getIsAvailable());
        dish.setMenu(menu);

        return toResponseDto(dishRepository.save(dish));
    }

    @Override
    public DishResponseDto updateDish(Integer id, DishDto dto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setIsAvailable(dto.getIsAvailable());
        dish.setMenu(menu);

        return toResponseDto(dishRepository.save(dish));
    }

    @Override
    public void deleteDish(Integer id) {
        dishRepository.deleteById(id);
    }

    @Override
    public List<DishResponseDto> getAllAvailableDishes() {
        return dishRepository.findByIsAvailableTrue()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public DishResponseDto getDishById(Integer id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        return toResponseDto(dish);
    }

    private DishResponseDto toResponseDto(Dish dish) {
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
