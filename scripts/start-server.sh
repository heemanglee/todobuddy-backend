#!/bin/bash

export JWT_SECRET_KEY=${JWT_SECRET_KEY}
export DB_URL=${DB_URL}
export DB_USERNAME=${DB_USERNAME}
export DB_PASSWORD=${DB_PASSWORD}
export MAIL_USERNAME=${MAIL_USERNAME}
export MAIL_PASSWORD=${MAIL_PASSWORD}
export REDIS_HOST=${REDIS_HOST}
export REDIS_PORT=${REDIS_PORT}
export JWT_EXPIRATION_TIME=${JWT_EXPIRATION_TIME}
export JWT_ISSUER=${JWT_ISSUER}

echo "--------------- 서버 배포 시작 -----------------"
cd /home/ubuntu/todobuddy-backend
sudo fuser -k -n tcp 8080 || true
nohup java -jar project.jar --spring.profiles.active=prod > ./output.log 2>&1 &
echo "--------------- 서버 배포 끝 -----------------"