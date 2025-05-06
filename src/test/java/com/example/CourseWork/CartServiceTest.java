package com.example.CourseWork;

import com.example.CourseWork.dto.CartItemDetailDto;
import com.example.CourseWork.dto.CartItemDto;
import com.example.CourseWork.dto.CartResponseDto;
import com.example.CourseWork.model.*;
import com.example.CourseWork.repository.*;
import com.example.CourseWork.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private DishRepository dishRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartByUserId_CartExists() {
        User user = new User();
        user.setId(1);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Pizza");
        dish.setPrice(10.0f);

        CartItem item = new CartItem();
        item.setDish(dish);
        item.setQuantity(2);
        item.setSpecialRequest("Extra cheese");

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setItems(List.of(item));

        when(cartRepository.findByUserId(1)).thenReturn(Optional.of(cart));

        CartResponseDto result = cartService.getCartByUserId(1);

        assertEquals(1, result.getUserId());
        assertEquals(1, result.getItems().size());

        CartItemDetailDto returnedItem = result.getItems().getFirst();
        assertEquals("Pizza", returnedItem.getDishName());
        assertEquals(2, returnedItem.getQuantity());
    }

    @Test
    void testAddItemToCart_NewCartCreated() {
        int userId = 1;
        CartItemDto itemDto = new CartItemDto();
        itemDto.setDishId(1);
        itemDto.setQuantity(3);
        itemDto.setSpecialRequest("No onions");

        User user = new User();
        user.setId(userId);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Burger");
        dish.setPrice(8.0f);

        Cart newCart = new Cart();
        newCart.setId(1);
        newCart.setUser(user);
        newCart.setItems(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(dishRepository.findById(1)).thenReturn(Optional.of(dish));
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        CartResponseDto response = cartService.addItemToCart(userId, itemDto);

        assertEquals(userId, response.getUserId());
        assertEquals(1, response.getItems().size());
        assertEquals("Burger", response.getItems().getFirst().getDishName());
        assertEquals(3, response.getItems().getFirst().getQuantity());
    }
}
