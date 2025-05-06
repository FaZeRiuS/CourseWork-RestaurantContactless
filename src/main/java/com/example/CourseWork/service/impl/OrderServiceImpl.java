package com.example.CourseWork.service.impl;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.*;
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
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public OrderResponseDto createOrder(Integer userId, OrderRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
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
        return toResponseDto(saved);
    }
    @Transactional
    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponseDto).collect(Collectors.toList());
    }
    @Transactional
    @Override
    public OrderResponseDto getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return toResponseDto(order);
    }
    @Transactional
    @Override
    public List<OrderResponseDto> getNewOrders() {
        return orderRepository.findAllByStatusOrderByCreatedAtDesc(OrderStatus.NEW)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto confirmOrderFromCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
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

        return toResponseDto(order);
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Integer Id, OrderStatus newStatus) {
        Order order = orderRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);

        return toResponseDto(updatedOrder);
    }

    private OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotalPrice(order.getTotalPrice());

        List<OrderItemResponseDto> items = order.getItems().stream().map(item -> {
            OrderItemResponseDto i = new OrderItemResponseDto();
            i.setDishId(item.getDish().getId());
            i.setDishName(item.getDish().getName());
            i.setQuantity(item.getQuantity());
            i.setSpecialRequest(item.getSpecialRequest());
            return i;
        }).collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }
}
