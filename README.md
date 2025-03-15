# Ollama Gemma3 Spring Boot Integration

This project demonstrates how to integrate Ollama with Gemma3 (1B parameter model) into a Spring Boot application.

## Prerequisites

- Java 17 or later
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
   http://localhost:8085
   ```

## API Endpoints

The application provides the following REST endpoints:

### Simple API (using Java's HttpClient)

```
POST /api/simple
```
Request body:
```json
{
  "message": "Your message here"
}
```

### Direct API (using HttpURLConnection)

```
POST /api/direct
```
Request body:
```json
{
  "message": "Your message here"
}
```

## Web Interface

The application includes a simple web interface:

- Home page: `http://localhost:8085/`
- Test page: `http://localhost:8085/test.html`

The test page provides an interactive interface to try out the API endpoints.

## Configuration

The application is configured to use Ollama with Gemma3 model. You can modify the configuration in `application.yml`:

```yaml
spring:
  application:
    name: OllamaDemo1
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

server:
  port: 8085

ollama:
  api-url: http://localhost:11434
  model: gemma3
```

## Technologies Used

- Spring Boot 3.4.3
- Ollama
- Gemma3 (1B parameter model)
- HTML/CSS/JavaScript for the web interface
