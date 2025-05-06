package com.example.CourseWork.service.impl;

import com.example.CourseWork.dto.*;
import com.example.CourseWork.model.*;
import com.example.CourseWork.repository.*;
import com.example.CourseWork.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CartResponseDto getCartByUserId(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        return toResponseDto(cart);
    }

    @Transactional
    @Override
    public CartResponseDto addItemToCart(Integer userId, CartItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });

        Dish dish = dishRepository.findById(itemDto.getDishId())
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        if (Boolean.FALSE.equals(dish.getIsAvailable())) {
            throw new RuntimeException("Dish is not available");
        }
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getDish().getId().equals(dish.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + itemDto.getQuantity());
            existingItem.setSpecialRequest(itemDto.getSpecialRequest());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setDish(dish);
            item.setQuantity(itemDto.getQuantity());
            item.setSpecialRequest(itemDto.getSpecialRequest());
            cart.getItems().add(item);
        }

        cartRepository.save(cart);

        return toResponseDto(cart);
    }

    private CartResponseDto toResponseDto(Cart cart) {
        CartResponseDto dto = new CartResponseDto();
        dto.setCartId(cart.getId());
        dto.setUserId(cart.getUser().getId());

        List<CartItemDetailDto> items = cart.getItems().stream().map(item -> {
            CartItemDetailDto detail = new CartItemDetailDto();
            detail.setDishId(item.getDish().getId());
            detail.setDishName(item.getDish().getName());
            detail.setPrice(item.getDish().getPrice());
            detail.setQuantity(item.getQuantity());
            detail.setSpecialRequest(item.getSpecialRequest());
            return detail;
        }).collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }
}
