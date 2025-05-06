package com.example.CourseWork.repository;

import com.example.CourseWork.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findByIsAvailableTrue();
}
