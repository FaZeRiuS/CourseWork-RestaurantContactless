package com.example.CourseWork.service;

import com.example.CourseWork.dto.CartItemDto;
import com.example.CourseWork.dto.CartResponseDto;

public interface CartService {
    CartResponseDto getCartByUserId(Integer userId);
    CartResponseDto addItemToCart(Integer userId, CartItemDto itemDto);
}
