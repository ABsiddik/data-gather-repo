package com.itkhamar.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class HotelApiWebClient implements WebFluxConfigurer {

    @Value("${RapidAPI.Key}")
    private String apiKey;
    @Value("${RapidAPI.Host}")
    private String apiHost;

    private static final String secureBaseUrl = "https://hotels4.p.rapidapi.com";

    public WebClient webClient() throws Exception{
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        return WebClient.builder()
                .baseUrl(secureBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().secure(t -> t.sslContext(sslContext)).responseTimeout(Duration.ofSeconds(35))))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-RapidAPI-Key", apiKey)
                .defaultHeader("X-RapidAPI-Host", apiHost)
                .build();
    }

}
