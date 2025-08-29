package com.example.webhook;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class SimpleWebhookChallenge {

    private static final String WEBHOOK_GENERATION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String WEBHOOK_TEST_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    public static void main(String[] args) {
        System.out.println("🚀 Starting Webhook Challenge Application");
        
        try {
            // Step 1: Generate webhook
            String[] webhookData = generateWebhook();
            
            if (webhookData != null && webhookData.length == 2) {
                String webhookUrl = webhookData[0];
                String accessToken = webhookData[1];
                
                System.out.println("✅ Webhook generated successfully");
                System.out.println("📧 Webhook URL: " + webhookUrl);
                System.out.println("🔑 Access Token received");
                
                // Step 2: Solve SQL problem
                String sqlSolution = solveSqlProblem("22BCE8419");
                
                // Step 3: Submit solution
                submitSolution(webhookUrl, accessToken, sqlSolution);
                
            } else {
                System.err.println("❌ Failed to generate webhook");
            }
            
        } catch (Exception e) {
            System.err.println("💥 Error in webhook challenge: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] generateWebhook() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        String requestBody = """
            {
                "name": "John Doe",
                "regNo": "22BCE8419",
                "email": "john@example.com"
            }
            """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(WEBHOOK_GENERATION_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        System.out.println("📤 Sending webhook generation request...");
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            String responseBody = response.body();
            System.out.println("📥 Response received: " + responseBody);
            
            // Simple JSON parsing (without external libraries)
            String webhook = extractJsonValue(responseBody, "webhook");
            String accessToken = extractJsonValue(responseBody, "accessToken");
            
            return new String[]{webhook, accessToken};
        } else {
            System.err.println("❌ Webhook generation failed with status: " + response.statusCode());
            System.err.println("📋 Response: " + response.body());
            return null;
        }
    }

    private static String solveSqlProblem(String regNo) {
        // Get last two digits
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        
        System.out.println("🔢 Registration number: " + regNo);
        System.out.println("🔢 Last two digits: " + lastTwoDigits);
        
        if (lastTwoDigitsInt % 2 == 1) {
            // Odd - Question 1
            System.out.println("❓ Solving Question 1 (odd regNo ending)");
            return solveQuestion1();
        } else {
            // Even - Question 2
            System.out.println("❓ Solving Question 2 (even regNo ending)");
            return "-- Question 2 solution would go here";
        }
    }

    private static String solveQuestion1() {
        // Question 1: Find highest salary not credited on 1st day of month
        String sqlQuery = """
            SELECT 
                p.AMOUNT as SALARY,
                CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME,
                FLOOR(DATEDIFF(CURDATE(), e.DOB) / 365.25) as AGE,
                d.DEPARTMENT_NAME
            FROM PAYMENTS p
            JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
            JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            WHERE DAY(p.PAYMENT_TIME) != 1
            ORDER BY p.AMOUNT DESC
            LIMIT 1
            """;
            
        System.out.println("📝 Question 1 SQL Solution:");
        System.out.println(sqlQuery);
        return sqlQuery.trim();
    }

    private static void submitSolution(String webhookUrl, String accessToken, String sqlSolution) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        String requestBody = """
            {
                "finalQuery": "%s"
            }
            """.formatted(sqlSolution.replace("\"", "\\\"").replace("\n", "\\n"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        System.out.println("📤 Submitting solution to webhook...");
        System.out.println("🔑 Using access token for authentication");
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            System.out.println("✅ Solution submitted successfully!");
            System.out.println("📋 Response: " + response.body());
        } else {
            System.err.println("❌ Solution submission failed with status: " + response.statusCode());
            System.err.println("📋 Response: " + response.body());
        }
    }

    private static String extractJsonValue(String json, String key) {
        // Simple JSON value extraction without external libraries
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        
        if (keyIndex == -1) return null;
        
        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return null;
        
        int valueStart = json.indexOf("\"", colonIndex) + 1;
        int valueEnd = json.indexOf("\"", valueStart);
        
        return json.substring(valueStart, valueEnd);
    }
}
