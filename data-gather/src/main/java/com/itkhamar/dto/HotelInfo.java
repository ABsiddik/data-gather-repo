package com.itkhamar.dto;

import lombok.Data;

@Data
public class HotelInfo {

    private String hotelId;
    private String cityId;
    private String type;

    private HotelRegion regionNames;
    private HotelCoOrdinate coordinates;
    private HotelAddress hotelAddress;
}
