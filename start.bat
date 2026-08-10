@echo off
title FurniHub - Full Stack Startup
echo ==========================================
echo    FurniHub - Full Stack Startup (Windows)
echo ==========================================
echo.

echo [0/3] Checking prerequisites...
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java not found. Install Java 17+ and add to PATH.
    pause
    exit /b 1
)
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Maven not found. Install Maven 3.8+ and add to PATH.
    pause
    exit /b 1
)
where npm >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Node.js/npm not found. Install Node.js 18+ and add to PATH.
    pause
    exit /b 1
)
echo Prerequisites check passed.
echo.

echo [1/3] Starting Backend Server (Spring Boot on port 8080)...
start "FurniHub Backend" cmd /k "cd /d backend && mvn spring-boot:run"
echo Waiting for backend to compile and start...
timeout /t 20 /nobreak >nul
echo.

echo [2/3] Starting Frontend Server (React on port 3000)...
start "FurniHub Frontend" cmd /k "cd /d frontend && npm start"
echo.

echo [3/3] Services starting...
echo.
echo ==========================================
echo    All services started!
echo ==========================================
echo Frontend:  http://localhost:3000
echo Backend:   http://localhost:8080/api
echo Admin:     http://localhost:3000/admin
echo.
echo Default Admin Credentials:
echo   Email:    admin@furnihub.com
echo   Password: admin123
echo.
echo Press any key to check service status, or close this window.
pause
