package com.framework.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.List;

@Configuration
public class RestClientConfig {

    @Value("${leon.api-url}")
    private String apiUrl;

    @Bean
    public RestClient leonRestClient() {
        HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(List.of(
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_OCTET_STREAM,
            MediaType.valueOf("text/plain")
        ));

        return RestClient.builder()
            .requestFactory(factory)
            .baseUrl(apiUrl)
            .messageConverters(converters -> converters.add(0, jsonConverter))
            .defaultHeaders(headers -> {
                headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
                headers.set("Accept", "application/json, text/plain, */*");
                headers.set("Accept-Language", "en-US,en;q=0.9");
                headers.set("X-Requested-With", "XMLHttpRequest");
            })
            .build();
    }
}
