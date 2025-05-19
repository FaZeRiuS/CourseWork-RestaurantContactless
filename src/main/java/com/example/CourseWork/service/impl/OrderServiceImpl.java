package com.example.CourseWork.service.impl;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.*;
import com.example.CourseWork.mapper.OrderMapper;
import com.example.CourseWork.model.*;
import com.example.CourseWork.repository.*;
import com.example.CourseWork.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto createOrder(String userId, OrderRequestDto dto) {
        Order order = new Order();
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        List<OrderItem> items = dto.getItems().stream().map(itemDto -> {
            Dish dish = dishRepository.findById(itemDto.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found: " + itemDto.getDishId()));

            OrderItem item = new OrderItem();
            item.setDish(dish);
            item.setQuantity(itemDto.getQuantity());
            item.setSpecialRequest(itemDto.getSpecialRequest());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        float total = items.stream()
                .map(item -> item.getDish().getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        order.setTotalPrice(total);
        order.setItems(items);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponseDto(saved);
    }

    @Transactional
    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(orderMapper::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderResponseDto getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toResponseDto(order);
    }

    @Transactional
    @Override
    public List<OrderResponseDto> getNewOrders() {
        List<Order> newOrders = orderRepository.findAllByStatusOrderByCreatedAtDesc(OrderStatus.NEW);
        if (newOrders.isEmpty()) {
            throw new RuntimeException("New orders not found");
        }
        return newOrders.stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto confirmOrderFromCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setDish(cartItem.getDish());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setSpecialRequest(cartItem.getSpecialRequest());
                    orderItem.setOrder(order);
                    return orderItem;
                }).collect(Collectors.toList());

        float total = orderItems.stream()
                .map(item -> item.getDish().getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        order.setTotalPrice(total);
        order.setItems(orderItems);

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toResponseDto(order);
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Integer Id, OrderStatus newStatus) {
        Order order = orderRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponseDto(updatedOrder);
    }
}
