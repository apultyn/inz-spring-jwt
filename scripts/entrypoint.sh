#!/bin/bash

echo "Waiting for database to be ready..."

# Check database status
while ! mysqladmin ping -h"$DB_HOST" -u"$DB_USERNAME" -p"$DB_PASSWORD" --silent; do
    echo "Database is not ready yet. Retrying in 5 seconds..."
    sleep 5
done

echo "Database is ready. Starting the application..."
exec java -jar app.jar
