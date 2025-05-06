package com.example.CourseWork;

import com.example.CourseWork.addition.Role;
import com.example.CourseWork.dto.DishDto;
import com.example.CourseWork.dto.DishResponseDto;
import com.example.CourseWork.model.Dish;
import com.example.CourseWork.model.Menu;
import com.example.CourseWork.model.User;
import com.example.CourseWork.repository.DishRepository;
import com.example.CourseWork.repository.MenuRepository;
import com.example.CourseWork.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.CourseWork.addition.Role.ADMINISTRATOR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class EditingMenuIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void adminCanAddEditAndDeleteDish() throws Exception {
        User admin = new User();
        admin.setName("Admin");
        admin.setRole(ADMINISTRATOR);
        userRepository.save(admin);

        Menu menu = new Menu();
        menu.setName("Lunch Menu");
        menuRepository.save(menu);

        DishDto newDish = new DishDto();
        newDish.setName("Soup");
        newDish.setDescription("Tomato soup");
        newDish.setPrice(5.5f);
        newDish.setIsAvailable(true);
        newDish.setMenuId(menu.getId());

        MvcResult addResult = mockMvc.perform(post("/api/dishes")
                        .with(user("Admin").roles(String.valueOf(ADMINISTRATOR)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDish))
                        .header("Authorization", "Bearer admin-token")) // Simulate admin auth
                .andExpect(status().isOk())
                .andReturn();

        DishResponseDto addedDish = objectMapper.readValue(
                addResult.getResponse().getContentAsString(), DishResponseDto.class
        );

        newDish.setName("Updated Soup");
        mockMvc.perform(put("/api/dishes/" + addedDish.getId())
                        .with(user("Admin").roles(String.valueOf(ADMINISTRATOR)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDish))
                        .header("Authorization", "Bearer admin-token"))
                .andExpect(status().isOk());

        Dish anotherDish = new Dish();
        anotherDish.setDescription("Desc");
        anotherDish.setPrice(3.0f);
        anotherDish.setIsAvailable(true);
        anotherDish.setMenu(menu);
        dishRepository.save(anotherDish);

        mockMvc.perform(delete("/api/dishes/" + anotherDish.getId())
                        .with(user("Admin").roles(String.valueOf(ADMINISTRATOR)))
                        .header("Authorization", "Bearer admin-token"))
                .andExpect(status().isNoContent());

        assertTrue(dishRepository.existsById(addedDish.getId()));
        assertFalse(dishRepository.existsById(anotherDish.getId()));
    }
    @Test
    public void customerCannotAccessMenuEdit() throws Exception {
        User customer = new User();
        customer.setName("Customer");
        customer.setRole(Role.CUSTOMER);
        userRepository.save(customer);

        DishDto newDish = new DishDto();
        newDish.setName("Unauthorized Dish");
        newDish.setDescription("No Access");
        newDish.setPrice(10.0f);
        newDish.setIsAvailable(true);
        newDish.setMenuId(1);

        mockMvc.perform(post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDish))
                        .header("Authorization", "Bearer customer-token"))
                .andExpect(status().isForbidden());
    }

}
