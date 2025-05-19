package com.example.CourseWork.service;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.*;
import com.example.CourseWork.mapper.OrderMapper;
import com.example.CourseWork.model.*;
import com.example.CourseWork.repository.*;
import com.example.CourseWork.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private DishRepository dishRepository;
    @Mock private CartRepository cartRepository;
    @Mock private OrderMapper orderMapper;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, dishRepository, cartRepository, orderMapper);
    }

    @Test
    void testCreateOrder_Success() {
        String userId = "user-123";

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Pizza");
        dish.setPrice(10.0f);

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setDishId(1);
        itemDto.setQuantity(2);
        itemDto.setSpecialRequest("Extra cheese");

        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setItems(List.of(itemDto));

        Order order = new Order();
        order.setId(1);
        order.setUserId(userId);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(20.0f);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(1);
        responseDto.setUserId(userId);
        responseDto.setStatus(OrderStatus.NEW);
        responseDto.setTotalPrice(20.0f);

        when(dishRepository.findById(1)).thenReturn(Optional.of(dish));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto response = orderService.createOrder(userId, orderRequestDto);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(20.0f, response.getTotalPrice());
    }

    @Test
    void testCreateOrder_DishNotFound() {
        String userId = "user-123";

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setDishId(1);
        itemDto.setQuantity(2);
        itemDto.setSpecialRequest("Extra cheese");

        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setItems(List.of(itemDto));

        when(dishRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                orderService.createOrder(userId, orderRequestDto));
    }

    @Test
    void testGetOrderById() {
        String userId = "user-123";
        Order order = new Order();
        order.setId(1);
        order.setUserId(userId);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(30.0f);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Burger");
        dish.setPrice(15.0f);

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(2);
        item.setSpecialRequest(null);
        item.setOrder(order);

        order.setItems(List.of(item));

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(1);
        responseDto.setUserId(userId);
        responseDto.setStatus(OrderStatus.NEW);
        responseDto.setTotalPrice(30.0f);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void testGetNewOrders() {
        String userId = "user-123";
        Order order = new Order();
        order.setId(1);
        order.setUserId(userId);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(30.0f);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Pizza");
        dish.setPrice(15.0f);

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(2);
        item.setSpecialRequest("No cheese");
        item.setOrder(order);

        order.setItems(List.of(item));

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(1);
        responseDto.setUserId(userId);
        responseDto.setStatus(OrderStatus.NEW);
        responseDto.setTotalPrice(30.0f);

        when(orderRepository.findAllByStatusOrderByCreatedAtDesc(OrderStatus.NEW))
                .thenReturn(List.of(order));
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        List<OrderResponseDto> result = orderService.getNewOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.getFirst().getUserId());
    }

    @Test
    void testGetAllOrders() {
        String userId = "user-123";
        Order order = new Order();
        order.setId(1);
        order.setUserId(userId);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(25.0f);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Salad");
        dish.setPrice(12.5f);

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(2);
        item.setOrder(order);
        order.setItems(List.of(item));

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(1);
        responseDto.setUserId(userId);
        responseDto.setStatus(OrderStatus.NEW);
        responseDto.setTotalPrice(25.0f);

        when(orderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(order));
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        List<OrderResponseDto> orders = orderService.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals(userId, orders.getFirst().getUserId());
    }

    @Test
    void testConfirmOrderFromCart() {
        String userId = "user-123";
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Dish1");
        dish.setPrice(10.0f);

        CartItem cartItem = new CartItem();
        cartItem.setDish(dish);
        cartItem.setQuantity(2);
        cartItem.setSpecialRequest("No onions");
        cart.getItems().add(cartItem);

        Order order = new Order();
        order.setId(1);
        order.setUserId(userId);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(20.0f);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(1);
        responseDto.setUserId(userId);
        responseDto.setStatus(OrderStatus.NEW);
        responseDto.setTotalPrice(20.0f);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.confirmOrderFromCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(OrderStatus.NEW, result.getStatus());
    }

    @Test
    void testChangeOrderStatus() {
        String userId = "user-123";
        Order order = new Order();
        order.setId(1);
        order.setUserId(userId);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(25.0f);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Salad");
        dish.setPrice(12.5f);

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(2);
        item.setOrder(order);
        order.setItems(List.of(item));

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(1);
        responseDto.setUserId(userId);
        responseDto.setStatus(OrderStatus.COMPLETED);
        responseDto.setTotalPrice(25.0f);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto updatedOrderResponse = orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);

        assertNotNull(updatedOrderResponse);
        assertEquals(OrderStatus.COMPLETED, updatedOrderResponse.getStatus());
        assertEquals(order.getId(), updatedOrderResponse.getId());
        assertEquals(userId, updatedOrderResponse.getUserId());
    }
}
