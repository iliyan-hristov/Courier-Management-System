package com.example.Project_api.web.dto;

import lombok.Data;

@Data
public class StatisticsResponse {

    private long totalOrders;
    private double totalRevenue;
    private double averageOrderValue;
    private double averageDeliveryTime;


    public StatisticsResponse(long totalOrders, double totalRevenue, double averageOrderValue, double averageDeliveryTime) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.averageOrderValue = averageOrderValue;
        this.averageDeliveryTime = averageDeliveryTime;
    }

    public StatisticsResponse() {

    }
}
