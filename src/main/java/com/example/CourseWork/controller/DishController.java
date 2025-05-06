package com.example.CourseWork.controller;

import com.example.CourseWork.dto.DishDto;
import com.example.CourseWork.dto.DishResponseDto;
import com.example.CourseWork.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<DishResponseDto> createDish(@RequestBody DishDto dto) {
        return ResponseEntity.ok(dishService.createDish(dto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<DishResponseDto> updateDish(@PathVariable Integer id, @RequestBody DishDto dto) {
        return ResponseEntity.ok(dishService.updateDish(id, dto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Integer id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<DishResponseDto>> getAvailableDishes() {
        return ResponseEntity.ok(dishService.getAllAvailableDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponseDto> getDishById(@PathVariable Integer id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }
}
