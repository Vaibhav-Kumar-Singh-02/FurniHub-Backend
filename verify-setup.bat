@echo off
echo ==========================================
echo    FurniHub - Setup Verification
echo ==========================================
echo.

echo [Check 1] Java Version...
java -version 2>&1 | findstr "version"
echo.

echo [Check 2] Node.js Version...
node --version 2>&1
echo.

echo [Check 3] MySQL Connection...
mysql -u root -pvaibhv -e "SELECT 'MySQL Connected' AS Status;" 2>nul
if %errorlevel% neq 0 (
    echo [WARNING] Could not connect to MySQL. Ensure MySQL is running.
)
echo.

echo [Check 4] Backend Compilation...
cd backend
.\mvnw compile -q
if %errorlevel% equ 0 (
    echo [OK] Backend compiles successfully
) else (
    echo [ERROR] Backend compilation failed
)
cd ..
echo.

echo [Check 5] Frontend Dependencies...
cd frontend
if exist node_modules (
    echo [OK] node_modules exists
) else (
    echo [INFO] node_modules not found. Run 'npm install' in frontend/
)
cd ..
echo.

echo ==========================================
echo    Verification Complete
echo ==========================================
pause
