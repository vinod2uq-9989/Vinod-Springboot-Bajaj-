@echo off
echo 🚀 Building Spring Boot Webhook Challenge...

rem Create build directories
if not exist "target\classes" mkdir target\classes
if not exist "lib" mkdir lib

rem Download Spring Boot dependencies (using direct download for simplicity)
echo 📦 Setting up dependencies...

rem For now, let's create a simple JAR that can be run
echo 📁 Compiling Java files...

rem Set classpath for Spring Boot
set CLASSPATH=.

rem Compile all Java files
javac -cp "%CLASSPATH%" -d target/classes src/main/java/com/example/webhook/*.java src/main/java/com/example/webhook/**/*.java

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)

echo ✅ Compilation successful!

rem Copy resources
if exist "src\main\resources" (
    xcopy "src\main\resources\*" "target\classes\" /E /Y
)

rem Create manifest
echo Main-Class: com.example.webhook.WebhookChallengeApplication > target\classes\META-INF\MANIFEST.MF

rem Create JAR
cd target\classes
jar cfm ..\webhook-challenge-0.0.1-SNAPSHOT.jar META-INF\MANIFEST.MF *
cd ..\..

echo 🎉 JAR created: target\webhook-challenge-0.0.1-SNAPSHOT.jar
echo 🚀 To run: java -jar target\webhook-challenge-0.0.1-SNAPSHOT.jar
pause
