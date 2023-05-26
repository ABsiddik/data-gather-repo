package com.itkhamar.service;

import com.itkhamar.config.FetchFileRoute;
import com.itkhamar.dto.HotelInfo;
import com.itkhamar.dto.MockInfo;
import com.itkhamar.utils.XmlConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataProcessService.class);

    private final FetchFileRoute fetchFileRoute;
//    private final DataCollectService dataCollectService;
//    private final XmlConverter xmlConverter;


    @Scheduled(initialDelay = 10000, fixedDelay = 1000 * 60 * 10)
    public void processData(){

        LOGGER.info("Process started at {}", new Date());
        try {

            fetchFileRoute.executeRoute();
//            dataCollectService.findHotelData("New York");
//            dataCollectService.findWeatherData("New York");
//            List<HotelInfo> hotelInfos = dataCollectService.convertToHotelInfos(null);
//            MockInfo info = new MockInfo();
//            info.setHotelInfos(hotelInfos);
//            xmlConverter.convertToXml(info);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
