package com.itkhamar.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class HotelCoOrdinate {
    private String lat;

    @Value("long")
    private String lng;
}
