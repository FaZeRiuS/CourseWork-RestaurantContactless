package com.example.CourseWork.controller;

import com.example.CourseWork.dto.MenuResponseDto;
import com.example.CourseWork.dto.MenuDto;
import com.example.CourseWork.dto.MenuWithDishesDto;
import com.example.CourseWork.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuWithDishesDto>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenusWithDishes());
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuDto dto) {
        return ResponseEntity.ok(menuService.createMenu(dto));
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Integer id, @RequestBody MenuDto dto) {
        return ResponseEntity.ok(menuService.updateMenu(id, dto));
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Integer id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
