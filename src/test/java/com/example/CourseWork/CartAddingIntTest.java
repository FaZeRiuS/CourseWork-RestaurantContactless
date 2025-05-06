package com.example.CourseWork;

import com.example.CourseWork.dto.CartItemDto;
import com.example.CourseWork.dto.CartResponseDto;
import com.example.CourseWork.model.Dish;
import com.example.CourseWork.model.User;
import com.example.CourseWork.repository.DishRepository;
import com.example.CourseWork.repository.UserRepository;
import com.example.CourseWork.service.CartService;
import com.example.CourseWork.service.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CartAddingIntTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishService dishService;

    @BeforeEach
    public void setUp() {
        dishRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testAddDishesToBasketIntegration() {
        User user = new User();
        user.setName("John Doe");
        userRepository.save(user);

        Dish dish1 = new Dish();
        dish1.setName("Dish 1");
        dish1.setDescription("Description 1");
        dish1.setPrice(15.0f);
        dish1.setIsAvailable(true);

        Dish dish2 = new Dish();
        dish2.setName("Dish 1");
        dish2.setDescription("Description 1");
        dish2.setPrice(15.0f);
        dish2.setIsAvailable(true);
        dishRepository.save(dish1);
        dishRepository.save(dish2);

        CartItemDto itemDto1 = new CartItemDto();
        itemDto1.setDishId(dish1.getId());
        itemDto1.setQuantity(2);
        itemDto1.setSpecialRequest("No onions");

        CartItemDto itemDto2 = new CartItemDto();
        itemDto2.setDishId(dish2.getId());
        itemDto2.setQuantity(1);
        itemDto2.setSpecialRequest("Extra cheese");

        cartService.addItemToCart(user.getId(), itemDto1);
        CartResponseDto cartResponse;
        cartResponse = cartService.addItemToCart(user.getId(), itemDto2);

        assertEquals(2, cartResponse.getItems().size());
        assertTrue(cartResponse.getItems().stream()
                .anyMatch(item -> item.getDishId().equals(dish1.getId()) && item.getQuantity() == 2));
        assertTrue(cartResponse.getItems().stream()
                .anyMatch(item -> item.getDishId().equals(dish2.getId()) && item.getQuantity() == 1));

        double total = cartResponse.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

        assertEquals(45.0, total, 0.01);
    }

    @Test
    public void testAddUnavailableDishToBasket() {
        User user = new User();
        user.setName("John Doe");
        userRepository.save(user);

        Dish unavailableDish = new Dish();
        unavailableDish.setName("Dish 3");
        unavailableDish.setDescription("Description 3");
        unavailableDish.setPrice(15.0f);
        unavailableDish.setIsAvailable(false);
        dishRepository.save(unavailableDish);

        CartItemDto itemDto = new CartItemDto();
        itemDto.setDishId(unavailableDish.getId());
        itemDto.setQuantity(1);
        itemDto.setSpecialRequest("No sauce");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.addItemToCart(user.getId(), itemDto);
        });

        assertEquals("Dish is not available", exception.getMessage());
    }
}
