#!/bin/bash

echo "=========================================="
echo "   FurniHub - Full Stack Startup"
echo "=========================================="
echo ""

echo "[1/3] Starting MySQL Database..."
echo "Make sure MySQL is running on localhost:3306"
echo ""

read -p "Press Enter to continue..."

echo "[2/3] Starting Backend Server..."
cd backend
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

echo "Waiting for backend to start..."
sleep 10

echo "[3/3] Starting Frontend Server..."
cd frontend
npm start &
FRONTEND_PID=$!
cd ..

echo ""
echo "=========================================="
echo "   All services started!"
echo "=========================================="
echo "Frontend: http://localhost:3000"
echo "Backend:  http://localhost:8080/api"
echo ""
echo "Admin Panel: http://localhost:3000/admin"
echo "Customer Site: http://localhost:3000"
echo ""
echo "Default Admin Credentials:"
echo "Email: admin@furnihub.com"
echo "Password: admin123"
echo ""

wait
