package com.example.CourseWork.mapper;

import com.example.CourseWork.dto.OrderItemResponseDto;
import com.example.CourseWork.dto.OrderResponseDto;
import com.example.CourseWork.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    
    public OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
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