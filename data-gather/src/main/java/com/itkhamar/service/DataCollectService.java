package com.itkhamar.service;

import com.itkhamar.dto.AddressInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCollectService {

    @Async
    public void collectData(List<AddressInfo> addressInfos){
        for (AddressInfo info : addressInfos){
            System.out.println("city: "+info);
        }
    }
}
