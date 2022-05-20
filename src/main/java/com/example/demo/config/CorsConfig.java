package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@Configuration
public class CorsConfig {

    private final static ApplicationLogger logger = new ApplicationLogger();

    private final Set<String> controllers;

    @Value("${allowed-origin}")
    private String[] allowedOrigin;

    @Autowired
    public CorsConfig(ApplicationContext applicationContext) {
        controllers = Stream.of(applicationContext.getBeanNamesForAnnotation(CrossOrigin.class))
                .filter(Objects::nonNull)
                .map(bean -> applicationContext.findAnnotationOnBean(bean, RequestMapping.class))
                .filter(mapping -> Objects.nonNull(mapping) && mapping.path().length > 0)
                .map(mapping -> mapping.path()[0])
                .peek(path -> logger.info(format("\"%s\" path registered for CORS configuration.", path)))
                .collect(toSet());
    }

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                for (String controller : controllers) {
                    registry.addMapping(controller)
                            .allowedOriginPatterns(allowedOrigin)
                            .allowedHeaders("Authorization", "Content-Type")
                            .allowedMethods("OPTIONS", "PUT", "POST", "DELETE", "GET");
                }
            }
        };
    }

    public List<String> getLogs() {
        return logger.logs();
    }

    private static class ApplicationLogger {

        private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

        private static final List<String> logs = new ArrayList<>();

        private List<String> logs() {
            return logs;
        }

        private void info(String log) {
            logger.info(log);
            logs.add(log);
        }
    }
}
