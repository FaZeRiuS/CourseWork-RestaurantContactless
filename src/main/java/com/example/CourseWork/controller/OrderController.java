package com.example.CourseWork.controller;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.OrderRequestDto;
import com.example.CourseWork.dto.OrderResponseDto;
import com.example.CourseWork.service.OrderService;
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

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Integer userId,
                                                        @RequestBody OrderRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PreAuthorize("hasRole('CHEF')")
    @GetMapping("/new")
    public List<OrderResponseDto> getNewOrders() {
        return orderService.getNewOrders();
    }

    @PostMapping("/confirm")
    public ResponseEntity<OrderResponseDto> confirmOrder(@RequestParam Integer userId) {
        return ResponseEntity.ok(orderService.confirmOrderFromCart(userId));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@RequestParam OrderStatus status, @PathVariable Integer id) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
