package com.itkhamar.config;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class FileUploadRoute {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadRoute.class);

    @Value("${local.xml.path}")
    private String localXmlPath;

    private final FTPConfig ftpConfig;

    public void executeRoute(){
        File path = new File(localXmlPath);
        try {
            Stream<Path> files = Files.list(path.toPath());
            if (files.count() == 0) {
                LOGGER.info("No files are found in this directory {}", localXmlPath);
                return;
            }
            LOGGER.info("Files uploading is started...");

            CamelContext context = new DefaultCamelContext();
            context.getManagementStrategy().addEventNotifier(new RouteStatusEventNotifier());
            context.setStreamCaching(true);
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("file://"+localXmlPath+"?charset=utf-8&noop=true")
                            .log("Files are being uploaded")
                            .to("ftp://"+ftpConfig.createConnectionString().concat("&passiveMode=true&transferLoggingLevel=INFO" +
                                    "&transferLoggingIntervalSeconds=1&transferLoggingVerbose=false&delay=5s"))
                            .log("Files are successfully uploaded");
                }
            });

            context.start();
            Thread.sleep(1000 * 20);

            if(context.isStarted()){
                context.stop();

                LOGGER.info("Files are being deleted");
                deleteFiles(path.toPath());
                LOGGER.info("Files are deleted from this directory {}", localXmlPath);
            }


        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    private void deleteFiles(Path path) throws Exception{
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .filter(File::isFile)
                .forEach(File::delete);

    }

}