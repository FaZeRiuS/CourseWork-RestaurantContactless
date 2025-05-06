package com.example.CourseWork.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private Float price;
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
}

