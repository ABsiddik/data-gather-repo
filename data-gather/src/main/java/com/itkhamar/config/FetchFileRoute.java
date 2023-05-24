package com.itkhamar.config;

import com.itkhamar.dto.AddressInfo;
import com.itkhamar.service.DataCollectService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FetchFileRoute {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchFileRoute.class);

    private final FTPConfig ftpConfig;
    private final DataCollectService dataCollectService;


    public void executeRoute(){
        CamelContext context = new DefaultCamelContext();
        context.getManagementStrategy().addEventNotifier(new RouteStatusEventNotifier());
        context.setStreamCaching(true);
        String uri = ftpConfig.createConnectionString().concat("&fileName=").concat(ftpConfig.getFileName());

        ConvertToAddress toAddress = new ConvertToAddress();
        LOGGER.info("Execution started..");
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("ftp://"+uri+"&autoCreate=false&passiveMode=true&transferLoggingLevel=INFO" +
                            "&transferLoggingIntervalSeconds=1&transferLoggingVerbose=false&delay=5s&noop=true")
                            .routeId("ftpCsvRoute")
                            .log("CSV file downloaded from FTP server: ${file:name}")
                            .unmarshal().csv()
                            .split(body())
                            .streaming()
                            .bean(toAddress, "createAddress")
                            .log("combining ...")
                            .aggregate(constant(true), new MyAggregationStrategy())
                            .completionTimeout(1000 * 10)
                            .log("File process done : ${body}")
                            .threads()
                            .bean(dataCollectService, "collectData")
                            .log("service is called");

                }
            });
            context.start();

            Thread.sleep(1000 * 20);
        } catch (Exception e){
            LOGGER.error("Error : "+e.getMessage());
        }
        finally {
            if(context.isStarted()){
                context.stop();
            }
        }
    }


    private class ConvertToAddress {
        public AddressInfo createAddress(String[] csvLine){
            AddressInfo obj = new AddressInfo();
            obj.setCountry(csvLine[0]);
            obj.setCity(csvLine[1]);
            obj.setDate(csvLine[2]);
            return obj;
        }
    }

    private class MyAggregationStrategy implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            List<AddressInfo> combinedList = new ArrayList<>();
            if (oldExchange == null) {
                AddressInfo newInfo = newExchange.getIn().getBody(AddressInfo.class);
                if(!newInfo.getCountry().equals("Country")) {
                    combinedList.add(newInfo);
                }
                newExchange.getIn().setBody(combinedList);
                return newExchange;
            }

            List<AddressInfo> oldList = oldExchange.getIn().getBody(List.class);
            AddressInfo newInfo = newExchange.getIn().getBody(AddressInfo.class);

            combinedList.addAll(oldList);
            combinedList.add(newInfo);
            oldExchange.getIn().setBody(combinedList);
            return oldExchange;
        }
    }
}
