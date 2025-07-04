package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AIConfig {

 @Value("${openai.api.key}")
 private String apiKey;

 @Value("${openai.api.url}")
 private String apiUrl;

 @Value("${openai.model}")
 private String model;

 public String getApiKey() { return apiKey; }
 public String getApiUrl() { return apiUrl; }
 public String getModel() { return model; }
}
