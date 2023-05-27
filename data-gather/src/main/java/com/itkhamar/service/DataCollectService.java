package com.itkhamar.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.itkhamar.config.FileUploadRoute;
import com.itkhamar.config.HotelApiWebClient;
import com.itkhamar.config.WeatherApiWebClient;
import com.itkhamar.dto.AddressInfo;
import com.itkhamar.dto.HotelInfo;
import com.itkhamar.dto.MockInfo;
import com.itkhamar.dto.WeatherInfo;
import com.itkhamar.utils.XmlConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataCollectService {

    @Value("${weather.appid}")
    private String weatherAppId;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCollectService.class);

    private final HotelApiWebClient hotelApiWebClient;
    private final WeatherApiWebClient weatherApiWebClient;
    private final XmlConverter xmlConverter;
    private final FileUploadRoute fileUploadRoute;

    @Async
    public void collectData(List<AddressInfo> addressInfos){
        for (AddressInfo info : addressInfos){
            MockInfo mock = new MockInfo();
            mock.setCountry(info.getCountry());
            mock.setCity(info.getCity());

            mock.setHotelInfos(findHotelData(info.getCity()));

            Optional<WeatherInfo> weatherInfo = findWeatherData(info.getCity());
            if(weatherInfo.isPresent()){
                mock.setWeatherInfo(weatherInfo.get());
            }

            xmlConverter.convertToXml(mock);
        }

        fileUploadRoute.executeRoute();
    }

    public List<HotelInfo> findHotelData(String city){
        LOGGER.info("Hotel data are fetching..");
        List<HotelInfo> hotelInfos = new ArrayList<>();
        try {
            ResponseEntity<String> resp = hotelApiWebClient.webClient()
                    .get()
                    .uri("/locations/v3/search?q="+city)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if(resp.getStatusCode() == HttpStatus.OK){
                hotelInfos = convertToHotelInfos(resp.getBody());
            }
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return hotelInfos;
    }


    public Optional<WeatherInfo> findWeatherData(String city){
        LOGGER.info("Weather data are fetching..");
        try {
            ResponseEntity<WeatherInfo> resp = weatherApiWebClient.webClient()
                    .get()
                    .uri("/weather?q="+city+"&appid="+weatherAppId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(WeatherInfo.class)
                    .block();

            if(resp.getStatusCode() == HttpStatus.OK){
                return Optional.ofNullable(resp.getBody());
            }
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<HotelInfo> convertToHotelInfos(String content){
        LOGGER.info("Hotel data are converting...");
        List<HotelInfo> hotelInfos = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode weatherInfo = mapper.readValue(content, JsonNode.class);

            if(weatherInfo.get("rc").toString().equals("\"OK\"") && weatherInfo.has("sr")){
                JsonNode sr = weatherInfo.get("sr");
                if(sr.isArray()){
                    ArrayNode data = (ArrayNode)sr;
                    for(JsonNode  n : data){
                        if(n.get("type").toString().equalsIgnoreCase("\"HOTEL\"")) {
                            HotelInfo info = mapper.treeToValue(n, HotelInfo.class);
                            hotelInfos.add(info);
                        }
                    }
                }
            }
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return hotelInfos;
    }


}
