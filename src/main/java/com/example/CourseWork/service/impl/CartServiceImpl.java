package com.example.CourseWork.service.impl;

import com.example.CourseWork.dto.CartItemDto;
import com.example.CourseWork.dto.CartResponseDto;
import com.example.CourseWork.mapper.CartMapper;
import com.example.CourseWork.model.Cart;
import com.example.CourseWork.model.CartItem;
import com.example.CourseWork.model.Dish;
import com.example.CourseWork.repository.CartRepository;
import com.example.CourseWork.repository.DishRepository;
import com.example.CourseWork.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final DishRepository dishRepository;
    private final CartMapper cartMapper;

    @Transactional
    @Override
    public CartResponseDto getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        return cartMapper.toResponseDto(cart);
    }

    @Transactional
    @Override
    public CartResponseDto addItemToCart(String userId, CartItemDto itemDto) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
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

        return cartMapper.toResponseDto(cart);
    }
}
