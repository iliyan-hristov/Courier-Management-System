package com.example.Project_api.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventRequest {

    @NotNull
    private Double price;

    @NotNull
    private Long deliveryTimeMinutes;


}
