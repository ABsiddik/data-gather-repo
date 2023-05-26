package com.itkhamar.utils;

import com.itkhamar.dto.MockInfo;
import com.itkhamar.dto.WeatherInfo;
import com.itkhamar.service.DataCollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;

@Component
public class XmlConverter {
    @Value("${local.xml.path}")
    private String localXmlPath;

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlConverter.class);

    public void convertToXml(MockInfo info){
        LOGGER.info("Converting to xml...");
        try{
            String file = localXmlPath.concat(info.getCountry().replaceAll(" ", "_")).concat(".xml");
            JAXBContext context = JAXBContext.newInstance(MockInfo.class);
            Marshaller mar= context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.marshal(info, new File(file));

            LOGGER.info("Converted path : {}", file);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }
}
