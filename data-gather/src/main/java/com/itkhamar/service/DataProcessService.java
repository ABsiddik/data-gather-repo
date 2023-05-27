package com.itkhamar.service;

import com.itkhamar.config.FetchFileRoute;
import com.itkhamar.config.FileUploadRoute;
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


    @Scheduled(initialDelay = 10000, fixedDelay = 1000 * 60 * 10)
    public void processData(){

        LOGGER.info("Process is started at {}", new Date());
        try {
            fetchFileRoute.executeRoute();

            LOGGER.info("Process is successfully completed at {}", new Date());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
