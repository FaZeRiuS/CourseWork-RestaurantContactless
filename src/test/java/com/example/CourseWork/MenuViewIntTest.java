package com.example.CourseWork;

import com.example.CourseWork.model.Dish;
import com.example.CourseWork.model.Menu;
import com.example.CourseWork.repository.MenuRepository;
import com.example.CourseWork.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuViewIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @BeforeEach
    public void setUp() {
        dishRepository.deleteAll();
        menuRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void testGetMenu_PositiveScenario() throws Exception {
        Menu menu = new Menu();
        menu.setName("Основне меню");
        menu = menuRepository.save(menu);

        Dish dish = new Dish();
        dish.setName("Борщ");
        dish.setDescription("Традиційний український суп");
        dish.setPrice(120.50f);
        dish.setIsAvailable(true);
        dish.setMenu(menu);
        dishRepository.save(dish);

        mockMvc.perform(get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Основне меню"))
                .andExpect(jsonPath("$[0].dishes[0].name").value("Борщ"))
                .andExpect(jsonPath("$[0].dishes[0].price").value(120.50));
    }

    @Test
    @WithMockUser
    public void testGetMenu_WhenMenuIsEmpty() throws Exception {
        mockMvc.perform(get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
