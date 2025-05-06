package com.example.CourseWork;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.dto.*;
import com.example.CourseWork.model.*;
import com.example.CourseWork.repository.*;
import com.example.CourseWork.service.OrderService;
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
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private DishRepository dishRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartRepository cartRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, orderItemRepository, dishRepository, userRepository, cartRepository);
    }

    @Test
    void testCreateOrder_Success() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

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


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dishRepository.findById(1)).thenReturn(Optional.of(dish));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(1);
            return o;
        });

        OrderResponseDto response = orderService.createOrder(userId, orderRequestDto);

        assertNotNull(response);
        assertEquals(1, response.getUserId());
        assertEquals(20.0f, response.getTotalPrice());
        assertEquals(1, response.getItems().size());
        assertEquals("Pizza", response.getItems().getFirst().getDishName());
    }

    @Test
    void testCreateOrder_UserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                orderService.createOrder(1, new OrderRequestDto()));
    }

    @Test
    void testCreateOrder_DishNotFound() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setDishId(1);
        itemDto.setQuantity(2);
        itemDto.setSpecialRequest("Extra cheese");

        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setItems(List.of(itemDto));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dishRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                orderService.createOrder(userId, orderRequestDto));
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(30.0f);

        User user = new User();
        user.setId(1);
        order.setUser(user);

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

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        OrderResponseDto result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getUserId());
        assertEquals(1, result.getItems().size());
        assertEquals("Burger", result.getItems().getFirst().getDishName());
    }

    @Test
    void testGetNewOrders() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(30.0f);

        User user = new User();
        user.setId(1);
        order.setUser(user);

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

        when(orderRepository.findAllByStatusOrderByCreatedAtDesc(OrderStatus.NEW))
                .thenReturn(List.of(order));

        List<OrderResponseDto> result = orderService.getNewOrders();

        assertNotNull(result);
        assertEquals(1, result.size());

        OrderResponseDto dto = result.getFirst();
        assertEquals(1, dto.getId());
        assertEquals(1, dto.getUserId());
        assertEquals(OrderStatus.NEW, dto.getStatus());
        assertEquals(30.0f, dto.getTotalPrice());
        assertEquals(1, dto.getItems().size());
        assertEquals("Pizza", dto.getItems().getFirst().getDishName());
        assertEquals("No cheese", dto.getItems().getFirst().getSpecialRequest());
    }


    @Test
    void testGetAllOrders() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(25.0f);

        User user = new User();
        user.setId(1);
        order.setUser(user);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Salad");
        dish.setPrice(12.5f);

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(2);
        item.setOrder(order);
        order.setItems(List.of(item));

        when(orderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(order));

        List<OrderResponseDto> orders = orderService.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals("Salad", orders.getFirst().getItems().getFirst().getDishName());
    }

    @Test
    void testConfirmOrderFromCart() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        Cart cart = new Cart();
        cart.setUser(user);
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

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto orderResponse = orderService.confirmOrderFromCart(user.getId());

        verify(cartRepository, times(1)).findByUserId(user.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).save(any(Cart.class));

        assertNotNull(orderResponse);
        assertEquals(OrderStatus.NEW, orderResponse.getStatus());
        assertEquals(user.getId(), orderResponse.getUserId());
        assertEquals(20.0f, orderResponse.getTotalPrice());
    }

    @Test
    void testChangeOrderStatus() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(25.0f);

        User user = new User();
        user.setId(1);
        order.setUser(user);

        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Salad");
        dish.setPrice(12.5f);

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(2);
        item.setOrder(order);
        order.setItems(List.of(item));


            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenReturn(order);

            OrderResponseDto updatedOrderResponse = orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);

            verify(orderRepository, times(1)).findById(order.getId());
            verify(orderRepository, times(1)).save(any(Order.class));

            assertNotNull(updatedOrderResponse);
            assertEquals(OrderStatus.COMPLETED, updatedOrderResponse.getStatus());
            assertEquals(order.getId(), updatedOrderResponse.getId());
    }
}
