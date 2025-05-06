package com.example.CourseWork.dto;

import lombok.Data;

@Data
public class DishDto {
    private String name;
    private String description;
    private Float price;
    private Boolean isAvailable;
    private Integer menuId;
}
