#!/bin/bash
# Script to start the Spring Boot server

cd /Users/mac/25fall-java/backend
echo "Starting server..."
mvn spring-boot:run -Dspring-boot.run.profiles=dev > /tmp/spring-boot.log 2>&1 &
echo "Server starting in background..."
echo "PID: $!"
echo ""
echo "To view logs: tail -f /tmp/spring-boot.log"
echo "To check status: curl http://localhost:8082/"
echo ""
echo "Wait 15-20 seconds for server to fully start"
