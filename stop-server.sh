#!/bin/bash
# Script to stop the Spring Boot server

echo "Stopping server on port 8082..."
lsof -ti :8082 | xargs kill -9 2>/dev/null
ps aux | grep -E "spring-boot|BackendApplication" | grep -v grep | awk '{print $2}' | xargs kill -9 2>/dev/null
echo "Server stopped."
