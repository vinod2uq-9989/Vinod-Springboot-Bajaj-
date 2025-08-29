# Spring Boot Webhook Challenge

## Overview
This Spring Boot application automatically handles the hiring challenge workflow:

1. **Startup Trigger**: Sends POST request to generate webhook
2. **Token Retrieval**: Receives webhook URL and JWT access token
3. **Problem Solving**: Determines SQL question based on registration number
4. **Solution Submission**: Submits SQL query using JWT authentication

## Project Structure
```
src/
├── main/
│   ├── java/com/example/webhook/
│   │   ├── WebhookChallengeApplication.java  # Main application
│   │   ├── config/AppConfig.java             # RestTemplate configuration
│   │   ├── service/WebhookChallengeService.java # Main business logic
│   │   └── model/                            # Data models
│   │       ├── WebhookGenerationRequest.java
│   │       ├── WebhookGenerationResponse.java
│   │       └── SolutionRequest.java
│   └── resources/
│       └── application.properties            # Configuration
├── pom.xml                                   # Maven dependencies
└── README.md                                # This file
```

## How It Works

### 1. Webhook Generation
- Sends POST to: `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- Payload: `{"name": "John Doe", "regNo": "REG12347", "email": "john@example.com"}`
- Receives: webhook URL and accessToken

### 2. Problem Selection
- Analyzes last 2 digits of regNo
- **Odd** (47) → Question 1: https://drive.google.com/file/d/1IeSI6l6KoSQAFfRihIT9tEDICtoz−G/view?usp=sharing
- **Even** → Question 2: https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view?usp=sharing

### 3. Solution Submission
- Sends POST to: `https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`
- Headers: `Authorization: <accessToken>`
- Payload: `{"finalQuery": "YOUR_SQL_QUERY_HERE"}`

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Commands
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Create JAR file
mvn clean package

# Run the JAR
java -jar target/webhook-challenge-0.0.1-SNAPSHOT.jar
```

## Important Notes

⚠️ **SQL Solutions**: The current implementation contains placeholder SQL queries. You need to:
1. Access the Google Drive links for the actual questions
2. Replace the placeholder SQL in `solveQuestion1()` and `solveQuestion2()` methods
3. Test with your actual registration details

## Configuration

Update the registration details in `WebhookChallengeService.java`:
```java
WebhookGenerationRequest request = new WebhookGenerationRequest(
    "Your Name", 
    "22BCE8419", 
    "your.email@example.com"
);
```

## Submission Requirements

For the hiring submission:
1. ✅ Public GitHub repository with code
2. ✅ JAR file in target/ directory
3. ✅ Downloadable JAR link
4. ✅ No controller endpoints (uses CommandLineRunner)
5. ✅ Uses RestTemplate for HTTP requests
6. ✅ JWT authentication implemented

## Testing

The application will:
1. Log all steps during execution
2. Show webhook generation status
3. Display SQL problem selection
4. Confirm solution submission
5. Exit after completion

## Architecture

```
Application Startup
        ↓
CommandLineRunner (WebhookChallengeService)
        ↓
Generate Webhook → Solve SQL → Submit Solution
        ↓                ↓            ↓
   RestTemplate     Problem       JWT Auth
   POST Request     Selection     POST Request
```

Built with Spring Boot 3.2.0 and Java 17.
