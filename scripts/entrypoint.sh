#!/bin/bash

# Database parameters
DB_HOST=spring-jwt-db
DB_USER=root
DB_PASSWORD=my-secret-pwd

echo "Waiting for database to be ready..."

# Check database status
while ! mysqladmin ping -h"$DB_HOST" -u"$DB_USER" -p"$DB_PASSWORD" --silent; do
    echo "Database is not ready yet. Retrying in 5 seconds..."
    sleep 5
done

echo "Database is ready. Starting the application..."
exec java -jar app.jar
