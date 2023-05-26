package com.itkhamar.dto;

import lombok.Data;

@Data
public class WeatherData {
    private float temp;
    private float feels_like;
    private float temp_min;
    private float temp_max;
    private int pressure;
    private int humidity;
}
