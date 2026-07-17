package com.example.Project_api.web;

import com.example.Project_api.model.Order;
import com.example.Project_api.service.StatisticsService;
import com.example.Project_api.web.dto.OrderEventRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final StatisticsService statisticsService;

    public OrderController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setCreatedAt(Instant.now());

        OrderEventRequest orderEvent = new OrderEventRequest();
        orderEvent.setPrice(order.getTotalPrice());
        orderEvent.setDeliveryTimeMinutes(order.getEstimatedDeliveryTime());

        statisticsService.saveOrderEvent(orderEvent);

        return order;
    }
}

