package com.example.CourseWork.service;

import com.example.CourseWork.dto.CartItemDetailDto;
import com.example.CourseWork.dto.CartItemDto;
import com.example.CourseWork.dto.CartResponseDto;
import com.example.CourseWork.mapper.CartMapper;
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
    @Mock private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartByUserId_CartExists() {
        String userId = "user-123";

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
        cart.setUserId(userId);
        cart.setItems(List.of(item));

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setUserId(userId);
        responseDto.setItems(List.of(new CartItemDetailDto()));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponseDto(any(Cart.class))).thenReturn(responseDto);

        CartResponseDto result = cartService.getCartByUserId(userId);

        assertEquals(userId, result.getUserId());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void testAddItemToCart_NewCartCreated() {
        String userId = "user-123";
        CartItemDto itemDto = new CartItemDto();
        itemDto.setDishId(1);
        itemDto.setQuantity(3);
        itemDto.setSpecialRequest("No onions");

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Burger");
        dish.setPrice(8.0f);

        Cart newCart = new Cart();
        newCart.setId(1);
        newCart.setUserId(userId);
        newCart.setItems(new ArrayList<>());

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setUserId(userId);
        responseDto.setItems(new ArrayList<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(dishRepository.findById(1)).thenReturn(Optional.of(dish));
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);
        when(cartMapper.toResponseDto(any(Cart.class))).thenReturn(responseDto);

        CartResponseDto response = cartService.addItemToCart(userId, itemDto);

        assertEquals(userId, response.getUserId());
        assertEquals(0, response.getItems().size());
    }
}
