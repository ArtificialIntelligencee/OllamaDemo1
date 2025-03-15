package com.demo.ollama1.OllamaDemo1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.ollama1.OllamaDemo1.model.ChatRequest;
import com.demo.ollama1.OllamaDemo1.model.ChatResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/simple")
public class SimpleOllamaController {

    private final String apiUrl;
    private final String model;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public SimpleOllamaController(
            @Value("${ollama.api-url}") String apiUrl,
            @Value("${ollama.model}") String model) {
        this.apiUrl = apiUrl;
        this.model = model;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            Map<String, String> requestMap = Map.of(
                "model", model,
                "prompt", request.getMessage()
            );
            
            String requestBody = objectMapper.writeValueAsString(requestMap);
            
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
            
            HttpResponse<String> response = httpClient.send(httpRequest, 
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                
                try {
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                    String responseText = (String) responseMap.get("response");
                    return new ChatResponse(responseText);
                } catch (IOException e) {
                    // If JSON parsing fails, return the raw response
                    return new ChatResponse("Error parsing response: " + responseBody);
                }
            } else {
                return new ChatResponse("Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            return new ChatResponse("Error: " + e.getMessage());
        }
    }

    static class OllamaRequest {
        private final String model;
        private final String prompt;

        public OllamaRequest(String model, String prompt) {
            this.model = model;
            this.prompt = prompt;
        }

        public String getModel() {
            return model;
        }

        public String getPrompt() {
            return prompt;
        }
    }

    static class OllamaResponse {
        @JsonProperty("response")
        private String response;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
} 