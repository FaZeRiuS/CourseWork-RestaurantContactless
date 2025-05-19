package com.example.CourseWork.service.impl;

import com.example.CourseWork.dto.*;
import com.example.CourseWork.mapper.MenuMapper;
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
    private final MenuMapper menuMapper;

    @Transactional
    @Override
    public List<MenuWithDishesDto> getAllMenusWithDishes() {
        List<Menu> menus = menuRepository.findAll();
        if (menus.isEmpty()) {
            throw new RuntimeException("The menu is currently not available");
        }
        return menus.stream()
                .map(menuMapper::toMenuWithDishesDto)
                .collect(Collectors.toList());
    }

    @Override
    public MenuResponseDto createMenu(MenuDto dto) {
        Menu menu = new Menu();
        menu.setName(dto.getName());
        return menuMapper.toResponseDto(menuRepository.save(menu));
    }

    @Override
    public MenuResponseDto updateMenu(Integer id, MenuDto dto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("The menu is currently not available"));
        menu.setName(dto.getName());
        Menu updated = menuRepository.save(menu);
        return menuMapper.toResponseDto(updated);
    }

    @Override
    public void deleteMenu(Integer id) {
        if (!menuRepository.existsById(id)) {
            throw new RuntimeException("The menu is currently not available");
        }
        menuRepository.deleteById(id);
    }
}