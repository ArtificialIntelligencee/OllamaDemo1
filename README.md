# Ollama Gemma3 Spring Boot Integration

This project demonstrates how to integrate Ollama with Gemma3 (1B parameter model) into a Spring Boot application using Spring AI.

## Prerequisites

- Java 23 or later
- Gradle
- Ollama installed locally
- Gemma3 model pulled in Ollama

## Setup Ollama with Gemma3

1. Install Ollama from [https://ollama.com/](https://ollama.com/)
2. Pull the Gemma3 model:
   ```
   ollama pull gemma3
   ```
3. Verify that Ollama is running:
   ```
   ollama list
   ```

## Running the Application

1. Clone this repository
2. Build the application:
   ```
   ./gradlew build
   ```
3. Run the application:
   ```
   ./gradlew bootRun
   ```
4. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

## API Endpoints

The application provides the following REST endpoints:

### Simple Chat

```
POST /api/ollama/simple
```
Request body:
```json
{
  "message": "Your message here"
}
```

### Chat with System Prompt

```
POST /api/ollama/system-prompt?systemPrompt=Your system prompt here
```
Request body:
```json
{
  "message": "Your message here"
}
```

### Chat with Template

```
POST /api/ollama/template?topic=Your topic here
```
Request body:
```json
{
  "message": "Your message here"
}
```

## Configuration

The application is configured to use Ollama with Gemma3 model. You can modify the configuration in `application.yml`:

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: gemma3
          temperature: 0.7
          top-p: 0.9
          max-tokens: 1000
      embedding:
        options:
          model: gemma3
```

## Technologies Used

- Spring Boot 3.4.3
- Spring AI (Spring AI Ollama Spring Boot Starter)
- Ollama
- Gemma3 (1B parameter model)
