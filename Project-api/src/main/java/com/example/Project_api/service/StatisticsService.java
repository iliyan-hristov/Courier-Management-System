package com.example.Project_api.service;

import com.example.Project_api.model.OrderEvent;
import com.example.Project_api.repository.OrderEventRepository;
import com.example.Project_api.web.dto.OrderEventRequest;
import com.example.Project_api.web.dto.StatisticsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;




@Service
public class StatisticsService {

    @Value("${statistics.service.url}")
    private String statisticsServiceUrl;

    private final OrderEventRepository orderEventRepository;

    public StatisticsService(OrderEventRepository repository) {
        this.orderEventRepository = repository;
    }

    public void saveOrderEvent(OrderEventRequest req) {
        OrderEvent e = new OrderEvent();
        e.setPrice(req.getPrice());
        e.setDeliveryTimeMinutes(req.getDeliveryTimeMinutes());
        e.setCreatedAt(Instant.now());
        orderEventRepository.save(e);

    }



    public StatisticsResponse computeStats() {
        long totalOrders = orderEventRepository.count();
        double totalRevenue = orderEventRepository.findAll().stream()
                .mapToDouble(OrderEvent::getPrice).sum();
        double avgOrderValue = totalOrders == 0 ? 0 : totalRevenue / totalOrders;
        double avgDeliveryTime = totalOrders == 0 ? 0 :
                orderEventRepository.findAll().stream()
                        .mapToLong(OrderEvent::getDeliveryTimeMinutes).average().orElse(0);

        StatisticsResponse r = new StatisticsResponse();
        r.setTotalOrders(totalOrders);
        r.setTotalRevenue(totalRevenue);
        r.setAverageOrderValue(avgOrderValue);
        r.setAverageDeliveryTime(avgDeliveryTime);
        return r;
    }

    public Object getAllOrders() {
        return orderEventRepository.findAll();
    }
}
