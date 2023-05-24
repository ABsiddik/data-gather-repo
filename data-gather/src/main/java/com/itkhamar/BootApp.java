package com.itkhamar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

/**
 * Application boot point
 *
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BootApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(BootApp.class, args);
        System.out.println("Application is booted....");
    }

}
