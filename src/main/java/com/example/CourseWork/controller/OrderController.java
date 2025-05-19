package com.example.CourseWork.controller;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.OrderRequestDto;
import com.example.CourseWork.dto.OrderResponseDto;
import com.example.CourseWork.service.OrderService;
import com.example.CourseWork.util.KeycloakUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(KeycloakUtil.getCurrentUser().getId(), dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_CHEF')")
    public ResponseEntity<List<OrderResponseDto>> getNewOrders() {
        return ResponseEntity.ok(orderService.getNewOrders());
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrderResponseDto> confirmOrderFromCart() {
        return ResponseEntity.ok(orderService.confirmOrderFromCart(KeycloakUtil.getCurrentUser().getId()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Integer id,
            @RequestParam OrderStatus newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }
}
