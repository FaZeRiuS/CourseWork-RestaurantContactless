package com.example.CourseWork.controller;

import com.example.CourseWork.dto.CartItemDto;
import com.example.CourseWork.dto.CartResponseDto;
import com.example.CourseWork.service.CartService;
import com.example.CourseWork.util.KeycloakUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartResponseDto> getCart() {
        return ResponseEntity.ok(cartService.getCartByUserId(KeycloakUtil.getCurrentUser().getId()));
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CartResponseDto> addItemToCart(@RequestBody CartItemDto itemDto) {
        return ResponseEntity.ok(cartService.addItemToCart(KeycloakUtil.getCurrentUser().getId(), itemDto));
    }
}
