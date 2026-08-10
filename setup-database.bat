@echo off
echo ==========================================
echo    FurniHub - Database Setup
echo ==========================================
echo.

echo Connecting to MySQL and setting up database...
echo.

mysql -u root -pRobin@Singh@755321 -e "CREATE DATABASE IF NOT EXISTS e_commerce;"

echo.
echo Running schema.sql...
mysql -u root -pRobin@Singh@755321 e_commerce < database\schema.sql

echo.
echo ==========================================
echo    Database Setup Complete!
echo ==========================================
echo.
echo Database: e_commerce
echo Tables created: users, jwt_tokens, otp_verification, products, productimages, categories, cart_items, orders, order_items, coupons, reviews
echo.
echo Default Admin User:
echo   Email: admin@furnihub.com
echo   Password: admin123
echo   Role: ADMIN
echo.
pause
