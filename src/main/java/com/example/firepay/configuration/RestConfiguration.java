package com.example.firepay.configuration;

import com.example.firepay.FirePayApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class RestConfiguration {


    @Value("${lean.base.host}")
    private String baseHost;
    @Value("${app.token}")
    private String appToken;

    @Bean("snakeCaseMapper")
    @Primary
    ObjectMapper getSnakeCaseMapper(Jackson2ObjectMapperBuilder objectMapperBuilder) {
        objectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapperBuilder.build();
    }

    // Instantiating and injecting restTemplate (our http client)
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder preconfiguredBuilder, ObjectMapper objectMapper) throws IOException {
        //This is needed because the baseUrl is not available when this bean is initialised
        Properties props = new Properties();
        props.load(FirePayApplication.class.getClassLoader().getResourceAsStream("application.yml"));
        var jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(objectMapper);
        return preconfiguredBuilder
                .additionalInterceptors(new LoggingInterceptor())
                .messageConverters(jsonMessageConverter)
                .rootUri(props.getProperty("lean.base.host"))
                .errorHandler(new RestErrorHandler())
                .requestFactory(() -> new BufferingClientHttpRequestFactory(preconfiguredBuilder.buildRequestFactory()))
                .build();
    }

    // Instantiating and injecting MTLS logic (Will be used by our http client)
    @Bean
    RestTemplateCustomizer sslRestTemplateCustomizer() {
        return new SSLCustomizer();
    }

}
