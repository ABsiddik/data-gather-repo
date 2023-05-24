package com.itkhamar.service;

import com.itkhamar.config.FetchFileRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataProcessService {

    private final FetchFileRoute fetchFileRoute;

    @Scheduled(initialDelay = 10000, fixedDelay = 1000 * 60 * 10)
    public void processData(){

        try {

            fetchFileRoute.executeRoute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
