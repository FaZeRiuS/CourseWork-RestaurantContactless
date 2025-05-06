package com.example.CourseWork;

import com.example.CourseWork.dto.DishDto;
import com.example.CourseWork.dto.DishResponseDto;
import com.example.CourseWork.model.Dish;
import com.example.CourseWork.model.Menu;
import com.example.CourseWork.repository.DishRepository;
import com.example.CourseWork.repository.MenuRepository;
import com.example.CourseWork.service.DishService;
import com.example.CourseWork.service.impl.DishServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private MenuRepository menuRepository;

    private DishService dishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dishService = new DishServiceImpl(dishRepository, menuRepository);
    }

    @Test
    void testGetDishById_DishExists() {
        Menu menu = new Menu();
        menu.setId(1);
        menu.setName("Italian Menu");

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Pizza");
        dish.setDescription("Delicious pizza with cheese");
        dish.setPrice(9.99f);
        dish.setIsAvailable(true);
        dish.setMenu(menu);

        Mockito.when(dishRepository.findById(1)).thenReturn(Optional.of(dish));

        DishResponseDto result = dishService.getDishById(1);

        assertNotNull(result);
        assertEquals("Pizza", result.getName());
        assertEquals(1, result.getMenuId());
    }


    @Test
    void testGetDishById_DishNotFound() {
        Mockito.when(dishRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dishService.getDishById(1));
    }

    @Test
    void testGetAllDishes() {
        Dish dish1 = new Dish();
        dish1.setIsAvailable(true);

        Menu menu = new Menu();
        menu.setId(1);
        dish1.setMenu(menu);

        Dish dish2 = new Dish();
        dish2.setIsAvailable(true);
        dish2.setMenu(menu);

        List<Dish> dishes = Arrays.asList(dish1, dish2);
        Mockito.when(dishRepository.findByIsAvailableTrue()).thenReturn(dishes);

        List<DishResponseDto> result = dishService.getAllAvailableDishes();

        assertEquals(2, result.size());
    }

    @Test
    void testCreateDish() {
        DishDto dishDto = new DishDto();
        dishDto.setName("Pasta");
        dishDto.setDescription("Delicious pasta with tomato sauce");
        dishDto.setPrice(12.99f);
        dishDto.setIsAvailable(true);
        dishDto.setMenuId(1);

        Menu menu = new Menu();
        menu.setId(1);
        menu.setName("Italian Menu");

        Dish dishEntity = new Dish();
        dishEntity.setId(1);
        dishEntity.setName("Pasta");
        dishEntity.setDescription("Delicious pasta with tomato sauce");
        dishEntity.setPrice(12.99f);
        dishEntity.setIsAvailable(true);
        dishEntity.setMenu(menu);

        Mockito.when(dishRepository.save(Mockito.any(Dish.class))).thenReturn(dishEntity);
        Mockito.when(menuRepository.findById(1)).thenReturn(Optional.of(menu));

        DishResponseDto result = dishService.createDish(dishDto);

        assertNotNull(result);
        assertEquals("Pasta", result.getName());
        assertEquals("Delicious pasta with tomato sauce", result.getDescription());
        assertEquals(12.99f, result.getPrice());
        assertTrue(result.getIsAvailable());
        assertEquals(1, result.getMenuId());
    }


    @Test
    void testDeleteDish() {
        Integer dishId = 1;
        Mockito.doNothing().when(dishRepository).deleteById(dishId);

        dishService.deleteDish(dishId);

        Mockito.verify(dishRepository, Mockito.times(1)).deleteById(dishId);
    }
}
