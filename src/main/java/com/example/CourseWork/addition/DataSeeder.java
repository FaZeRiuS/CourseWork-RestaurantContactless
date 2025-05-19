package com.example.CourseWork.addition;

import com.example.CourseWork.model.Dish;
import com.example.CourseWork.model.Menu;
import com.example.CourseWork.repository.DishRepository;
import com.example.CourseWork.repository.MenuRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @PostConstruct
    public void seedData() {
        if (menuRepository.count() == 0) {
            Menu mainMenu = new Menu();
            mainMenu.setName("Main Menu");
            menuRepository.save(mainMenu);

            Menu dessertsMenu = new Menu();
            dessertsMenu.setName("Desserts");
            menuRepository.save(dessertsMenu);

            Menu drinksMenu = new Menu();
            drinksMenu.setName("Drinks");
            menuRepository.save(drinksMenu);

            List<Dish> mainDishes = Arrays.asList(
                    createDish("Grilled Salmon", "Fresh Atlantic salmon with lemon butter sauce", 24.99f, true, mainMenu),
                    createDish("Beef Steak", "Premium beef steak with mushroom sauce", 29.99f, true, mainMenu),
                    createDish("Chicken Alfredo", "Creamy pasta with grilled chicken", 18.99f, true, mainMenu),
                    createDish("Vegetable Stir Fry", "Fresh vegetables with tofu in soy sauce", 16.99f, true, mainMenu),
                    createDish("Fish and Chips", "Crispy battered fish with golden fries", 19.99f, true, mainMenu)
            );

            List<Dish> desserts = Arrays.asList(
                    createDish("Chocolate Cake", "Rich chocolate cake with ganache", 8.99f, true, dessertsMenu),
                    createDish("Cheesecake", "Classic New York style cheesecake", 7.99f, true, dessertsMenu),
                    createDish("Tiramisu", "Italian coffee-flavored dessert", 9.99f, true, dessertsMenu),
                    createDish("Ice Cream Sundae", "Vanilla ice cream with chocolate sauce", 6.99f, true, dessertsMenu)
            );

            List<Dish> drinks = Arrays.asList(
                    createDish("Fresh Orange Juice", "Freshly squeezed orange juice", 4.99f, true, drinksMenu),
                    createDish("Iced Tea", "Refreshing iced tea with lemon", 3.99f, true, drinksMenu),
                    createDish("Coffee", "Freshly brewed coffee", 3.49f, true, drinksMenu),
                    createDish("Mineral Water", "Sparkling mineral water", 2.99f, true, drinksMenu)
            );

            dishRepository.saveAll(mainDishes);
            dishRepository.saveAll(desserts);
            dishRepository.saveAll(drinks);
        }
    }

    private Dish createDish(String name, String description, float price, boolean isAvailable, Menu menu) {
        Dish dish = new Dish();
        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(price);
        dish.setIsAvailable(isAvailable);
        dish.setMenu(menu);
        return dish;
    }
} 