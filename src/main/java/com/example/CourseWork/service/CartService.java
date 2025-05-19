package com.example.CourseWork.service;

import com.example.CourseWork.dto.CartItemDto;
import com.example.CourseWork.dto.CartResponseDto;

public interface CartService {
    CartResponseDto getCartByUserId(String userId);
    CartResponseDto addItemToCart(String userId, CartItemDto itemDto);
}
