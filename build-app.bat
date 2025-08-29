@echo off
echo 🚀 Building WebhookChallengeApp from scratch...

rem Clean previous builds
if exist "build" rmdir /s /q build
if exist "WebhookChallengeApp.jar" del WebhookChallengeApp.jar

rem Create build directory
mkdir build

echo 📁 Compiling Java application...
javac -d build WebhookChallengeApp.java

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)

echo ✅ Compilation successful!

rem Create manifest
echo Main-Class: com.hiring.webhook.WebhookChallengeApp > build\MANIFEST.MF

rem Create JAR
echo 📦 Creating JAR file...
cd build
jar cfm ..\WebhookChallengeApp.jar MANIFEST.MF com\hiring\webhook\*.class
cd ..

if %ERRORLEVEL% NEQ 0 (
    echo ❌ JAR creation failed
    pause
    exit /b 1
)

echo 🎉 JAR created successfully: WebhookChallengeApp.jar
echo 📏 JAR file info:
dir WebhookChallengeApp.jar

echo.
echo 🧪 Testing JAR file:
java -jar WebhookChallengeApp.jar

echo.
echo ✅ Build complete! Ready for GitHub upload.
pause
