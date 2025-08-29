package com.hiring.webhook;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebhookChallengeApp {

    private static final String WEBHOOK_GENERATION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    
    // Registration details - change these for different submission
    private static final String NAME = "Vinod Kumar";
    private static final String REG_NO = "22BCE8419"; // Odd number for Question 1
    private static final String EMAIL = "vinod.kumar@example.com";

    public static void main(String[] args) {
        System.out.println("🚀 Starting Webhook Challenge Application");
        System.out.println("👤 Name: " + NAME);
        System.out.println("🎫 Registration: " + REG_NO);
        System.out.println("📧 Email: " + EMAIL);
        
        try {
            // Step 1: Generate webhook
            String[] webhookData = generateWebhook();
            
            if (webhookData != null && webhookData.length == 2) {
                String webhookUrl = webhookData[0];
                String accessToken = webhookData[1];
                
                System.out.println("✅ Webhook generated successfully");
                System.out.println("📧 Webhook URL: " + webhookUrl);
                System.out.println("🔑 Access Token: " + accessToken.substring(0, 20) + "...");
                
                // Step 2: Solve SQL problem based on regNo
                String sqlSolution = solveSqlProblem(REG_NO);
                
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
        
        String requestBody = String.format("""
            {
                "name": "%s",
                "regNo": "%s",
                "email": "%s"
            }
            """, NAME, REG_NO, EMAIL);

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
            
            // Extract webhook and accessToken from JSON response
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
            return solveQuestion2();
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

    private static String solveQuestion2() {
        // Question 2: Count younger employees in same department
        String sqlQuery = """
            SELECT 
                e1.EMP_ID,
                e1.FIRST_NAME,
                e1.LAST_NAME,
                d.DEPARTMENT_NAME,
                COUNT(e2.EMP_ID) as YOUNGER_EMPLOYEES_COUNT
            FROM EMPLOYEE e1
            JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
            LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT 
                AND e2.DOB > e1.DOB
            GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
            ORDER BY e1.EMP_ID DESC
            """;
            
        System.out.println("📝 Question 2 SQL Solution:");
        System.out.println(sqlQuery);
        return sqlQuery.trim();
    }

    private static void submitSolution(String webhookUrl, String accessToken, String sqlSolution) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        // Properly escape the SQL query for JSON
        String escapedQuery = sqlSolution
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "")
            .replace("\t", "\\t");

        String requestBody = String.format("""
            {
                "finalQuery": "%s"
            }
            """, escapedQuery);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        System.out.println("📤 Submitting solution to webhook...");
        System.out.println("🔑 Using access token for authentication");
        System.out.println("📝 SQL Query length: " + sqlSolution.length() + " characters");
        
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
        
        if (valueStart == 0 || valueEnd == -1) return null;
        
        return json.substring(valueStart, valueEnd);
    }
}
