package com.itkhamar.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WeatherInfo {
    private String name;
    private String base;
    private int visibility;
    private long dt;
    private long id;
    private int cod;

    private CoOrdination coord;
//    private List<Map<String, Object>> weather;
    private WeatherData main;
    private Wind wind;
//    private Map<String, Object> clouds;
//    private Map<String, Object> sys;
}
