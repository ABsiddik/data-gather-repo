package com.itkhamar.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class MockInfo {
    @XmlAttribute
    private String country;
    private String city;

    @XmlElement(nillable = true)
    private List<HotelInfo> hotelInfos;

    @XmlElement(nillable = true)
    private WeatherInfo weatherInfo;
}
