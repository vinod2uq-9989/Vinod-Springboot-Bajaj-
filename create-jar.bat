@echo off
echo 🏗️ Creating JAR file for submission...

rem Create manifest file
echo Main-Class: com.example.webhook.SimpleWebhookChallenge > manifest.txt

rem Create JAR with manifest
jar cfm SimpleWebhookChallenge.jar manifest.txt -C target .

if %ERRORLEVEL% NEQ 0 (
    echo ❌ JAR creation failed
    pause
    exit /b 1
)

echo ✅ JAR created successfully: SimpleWebhookChallenge.jar
echo 📏 JAR file size:
dir SimpleWebhookChallenge.jar
echo.
echo 🧪 Test the JAR:
echo java -jar SimpleWebhookChallenge.jar
pause
