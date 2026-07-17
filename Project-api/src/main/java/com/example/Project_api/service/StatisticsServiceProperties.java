package com.example.Project_api.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "statistics.service")
@Component
@Getter
@Setter
public class StatisticsServiceProperties {
    private String url;

}
