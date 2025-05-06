package com.example.CourseWork;

import com.example.CourseWork.addition.OrderStatus;
import com.example.CourseWork.addition.Role;
import com.example.CourseWork.model.Order;
import com.example.CourseWork.model.User;
import com.example.CourseWork.repository.OrderRepository;
import com.example.CourseWork.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NewOrdersIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void chefCanSeeNewOrders() throws Exception {
        User chef = new User();
        chef.setName("Chef");
        chef.setRole(Role.CHEF);
        userRepository.save(chef);

        Order order1 = new Order();
        order1.setUser(chef);
        order1.setStatus(OrderStatus.NEW);
        order1.setItems(new ArrayList<>());
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setUser(chef);
        order2.setStatus(OrderStatus.NEW);
        order2.setItems(new ArrayList<>());
        orderRepository.save(order2);

        mockMvc.perform(get("/api/orders/new")
                        .with(user("Chef").roles("CHEF"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void chefSeesNoOrdersWhenNoneExist() throws Exception {
        User chef = new User();
        chef.setName("Chef");
        chef.setRole(Role.CHEF);
        userRepository.save(chef);

        mockMvc.perform(get("/api/orders/new")
                        .with(user("Chef").roles("CHEF"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
