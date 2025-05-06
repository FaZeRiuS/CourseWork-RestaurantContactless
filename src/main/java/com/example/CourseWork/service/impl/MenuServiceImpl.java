package com.example.CourseWork.service.impl;

import com.example.CourseWork.dto.*;
import com.example.CourseWork.model.Menu;
import com.example.CourseWork.repository.MenuRepository;
import com.example.CourseWork.service.MenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    @Override
    public List<MenuWithDishesDto> getAllMenusWithDishes() {
        return menuRepository.findAll().stream().map(menu -> {
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
        }).collect(Collectors.toList());
    }

    @Override
    public MenuResponseDto createMenu(MenuDto dto) {
        Menu menu = new Menu();
        menu.setName(dto.getName());
        return toResponseDto(menuRepository.save(menu));
    }


    @Override
    public MenuResponseDto updateMenu(Integer id, MenuDto dto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        menu.setName(dto.getName());
        Menu updated = menuRepository.save(menu);
        return toResponseDto(updated);
    }

    @Override
    public void deleteMenu(Integer id) {
        menuRepository.deleteById(id);
    }

    private MenuResponseDto toResponseDto(Menu menu) {
        MenuResponseDto dto = new MenuResponseDto();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        return dto;
    }
}