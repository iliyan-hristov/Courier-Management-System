package com.example.Project_api.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.Project_api.service.StatisticsService;
import com.example.Project_api.web.dto.OrderEventRequest;
import com.example.Project_api.web.dto.StatisticsResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/statistics")
public class StatisticController {

    private final StatisticsService statisticsService;

    public StatisticController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    @ResponseBody
    public StatisticsResponse getStatistics() {
        return statisticsService.computeStats();
    }

    @GetMapping("/page")
    public String getStatisticsPage(Model model) {
        StatisticsResponse stats = statisticsService.computeStats();
        model.addAttribute("statistics", stats);
        model.addAttribute("orders", statisticsService.getAllOrders());
        return "statistics";
    }

    @PostMapping("/events")
    @ResponseBody
    public void ingestEvent(@Valid @RequestBody OrderEventRequest request) {
        statisticsService.saveOrderEvent(request);
    }

    @GetMapping("/add-test-order")
    @ResponseBody
    public String addTestOrder() {
        OrderEventRequest req = new OrderEventRequest();
        req.setPrice(25.99);
        req.setDeliveryTimeMinutes(30L);
        statisticsService.saveOrderEvent(req);
        return "Test order added. Total orders: " + statisticsService.computeStats().getTotalOrders();
    }
}
