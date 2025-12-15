#!/bin/bash
# Script to restart the Spring Boot server

echo "Stopping existing server..."
# Kill processes on port 8082
lsof -ti :8082 | xargs kill -9 2>/dev/null
# Kill Spring Boot processes
ps aux | grep -E "spring-boot|BackendApplication" | grep -v grep | awk '{print $2}' | xargs kill -9 2>/dev/null

echo "Waiting for processes to stop..."
sleep 2

echo "Starting server..."
cd /Users/mac/25fall-java/backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev > /tmp/spring-boot.log 2>&1 &

echo "Server is starting in the background..."
echo "Check logs with: tail -f /tmp/spring-boot.log"
echo "Wait about 15-20 seconds for the server to fully start"
echo ""
echo "To check if server is ready, run:"
echo "  curl http://localhost:8082/"
echo ""
echo "Or open in browser: http://localhost:8082/"

