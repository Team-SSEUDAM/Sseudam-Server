#!/bin/bash

set -e

IMAGE_URL=$1
CONTAINER_NAME=$2

BLUE_PORT=8080
GREEN_PORT=8081
HEALTH_CHECK_PATH="/actuator/health"
NGINX_CONF="/etc/nginx/nginx.conf"

# 현재 활성 포트 확인
CURRENT_PORT=$(grep -oP 'server 127.0.0.1:\K[0-9]+' $NGINX_CONF | head -1)
echo "Current active port: $CURRENT_PORT"

# 새로운 배포 포트 결정
if [ "$CURRENT_PORT" == "$BLUE_PORT" ]; then
    NEW_PORT=$GREEN_PORT
    NEW_COLOR="GREEN"
    OLD_COLOR="BLUE"
else
    NEW_PORT=$BLUE_PORT
    NEW_COLOR="BLUE"
    OLD_COLOR="BLUE"
fi

echo "Deploying to $NEW_COLOR ($NEW_PORT)..."

# 새 컨테이너 배포
export IMAGE_FULL_URL=$IMAGE_URL
export DOCKERHUB_IMAGE_NAME="${CONTAINER_NAME}-${NEW_COLOR}"
export SERVER_PORT=$NEW_PORT

cd ~/app
docker compose -f docker/docker-compose-dev.yml up -d

# 헬스체크
echo "Health checking on port $NEW_PORT..."
for i in {1..30}; do
    if curl -f http://localhost:$NEW_PORT$HEALTH_CHECK_PATH > /dev/null 2>&1; then
        echo "Health check passed!"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "Health check failed after 30 attempts"
        docker compose -f docker/docker-compose-dev.yml down
        exit 1
    fi
    echo "Waiting for application to be ready... ($i/30)"
    sleep 2
done

# Nginx 포트 스위칭
echo "Switching Nginx to port $NEW_PORT..."
sudo sed -i "s/server 127.0.0.1:[0-9]\+;/server 127.0.0.1:$NEW_PORT;/" $NGINX_CONF
sudo nginx -t && sudo systemctl reload nginx

echo "Deployment successful! Active: $NEW_COLOR ($NEW_PORT)"

# 이전 컨테이너 정리
sleep 5
OLD_CONTAINER="${CONTAINER_NAME}-${OLD_COLOR}"
if docker ps -a --format '{{.Names}}' | grep -q "^${OLD_CONTAINER}$"; then
    echo "Removing old container: $OLD_CONTAINER"
    docker stop $OLD_CONTAINER || true
    docker rm $OLD_CONTAINER || true
fi

docker image prune -a -f
echo "Blue/Green deployment completed!"

docker run -d --name sseudam-redis -p 6379:6379 --restart always redis:alpine
