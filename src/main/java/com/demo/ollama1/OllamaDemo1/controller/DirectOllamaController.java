package com.demo.ollama1.OllamaDemo1.controller;

import com.demo.ollama1.OllamaDemo1.model.ChatRequest;
import com.demo.ollama1.OllamaDemo1.model.ChatResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/direct")
public class DirectOllamaController {

    private final String apiUrl;
    private final String model;
    private final ObjectMapper objectMapper;

    public DirectOllamaController(
            @Value("${ollama.api-url}") String apiUrl,
            @Value("${ollama.model}") String model) {
        this.apiUrl = apiUrl;
        this.model = model;
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            final HttpURLConnection connection = getHttpURLConnection(request);

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    JsonNode jsonNode = objectMapper.readTree(responseLine);
                    if (jsonNode.has("response")) {
                        response.append(jsonNode.get("response").asText());
                    }
                }
            }

            return new ChatResponse(response.toString());
        } catch (Exception e) {
            return new ChatResponse("Error: " + e.getMessage());
        }
    }

    private HttpURLConnection getHttpURLConnection(ChatRequest request) throws IOException {
        URL url = new URL(apiUrl + "/api/generate");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = String.format("{\"model\":\"%s\",\"prompt\":\"%s\"}",
            model, request.getMessage().replace("\"", "\\\""));

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
} 