package com.example.CourseWork;

import com.example.CourseWork.dto.*;
import com.example.CourseWork.model.*;
import com.example.CourseWork.repository.MenuRepository;
import com.example.CourseWork.service.impl.MenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    private MenuServiceImpl menuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuService = new MenuServiceImpl(menuRepository);
    }

    @Test
    void testCreateMenu() {
        MenuDto requestDto = new MenuDto();
        requestDto.setName("Lunch Menu");

        Menu savedMenu = new Menu();
        savedMenu.setId(1);
        savedMenu.setName("Lunch Menu");

        when(menuRepository.save(any(Menu.class))).thenReturn(savedMenu);

        MenuResponseDto result = menuService.createMenu(requestDto); // changed return type

        assertNotNull(result);
        assertEquals("Lunch Menu", result.getName());
        assertEquals(1, result.getId());
    }


    @Test
    void testUpdateMenu() {
        Integer menuId = 1;
        MenuDto menuDto = new MenuDto();
        menuDto.setName("Updated Menu");

        Menu existingMenu = new Menu();
        existingMenu.setId(menuId);
        existingMenu.setName("Lunch Menu");

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(existingMenu));
        when(menuRepository.save(any(Menu.class))).thenReturn(existingMenu);

        MenuResponseDto result = menuService.updateMenu(menuId, menuDto);

        assertNotNull(result);
        assertEquals("Updated Menu", result.getName());
        assertEquals(menuId, result.getId());
    }

    @Test
    void testGetAllMenusWithDishes() {
        Menu menu = new Menu();
        menu.setId(1);
        menu.setName("Lunch Menu");

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Pizza");

        menu.setDishes(List.of(dish));

        when(menuRepository.findAll()).thenReturn(List.of(menu));

        List<MenuWithDishesDto> result = menuService.getAllMenusWithDishes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lunch Menu", result.getFirst().getName());
        assertEquals(1, result.getFirst().getDishes().size());
        assertEquals("Pizza", result.getFirst().getDishes().getFirst().getName());
    }

    @Test
    void testDeleteMenu() {
        Integer menuId = 1;

        menuService.deleteMenu(menuId);

        verify(menuRepository, times(1)).deleteById(menuId);  // Verify delete was called once
    }

    @Test
    void testUpdateMenu_MenuNotFound() {
        Integer menuId = 1;
        MenuDto menuDto = new MenuDto();
        menuDto.setName("Non-existent Menu");

        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> menuService.updateMenu(menuId, menuDto));
    }
}
