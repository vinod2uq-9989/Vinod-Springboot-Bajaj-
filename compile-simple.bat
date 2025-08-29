@echo off
echo 🚀 Compiling Simple Webhook Challenge (without Spring Boot dependencies)...

rem Create target directory
if not exist "target" mkdir target

echo 📁 Compiling Java file...
javac -d target SimpleWebhookChallenge.java

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)

echo ✅ Compilation successful!
echo 🚀 To run: java -cp target com.example.webhook.SimpleWebhookChallenge
echo.
echo 💡 This version uses only built-in Java HTTP client (no Spring Boot required)
pause
