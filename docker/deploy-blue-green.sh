#!/bin/bash

set -e

IMAGE_URL=$1
CONTAINER_NAME=$2
ENVIRONMENT=${3:-dev}

BLUE_PORT=8080
GREEN_PORT=8081
HEALTH_CHECK_PATH="/actuator/health"

# 환경별 Nginx 설정 파일 경로
if [ "$ENVIRONMENT" == "prod" ]; then
    NGINX_CONF="/etc/nginx/nginx.conf"
else
    NGINX_CONF="/etc/nginx/conf.d/dev-api.sseudam.me.conf"
fi

echo "Starting Blue/Green deployment...$NGINX_CONF"

# 현재 활성 포트 확인
CURRENT_PORT=$(grep -oP 'server 127.0.0.1:\K[0-9]+' $NGINX_CONF | head -1 || echo "")

# 컨테이너가 없는 경우 기본값 설정
if [ -z "$CURRENT_PORT" ]; then
    echo "No active port found. Starting initial deployment on BLUE ($BLUE_PORT)"
    NEW_PORT=$BLUE_PORT
    NEW_COLOR="BLUE"
    OLD_COLOR=""
else
    echo "Current active port: $CURRENT_PORT"
    # 새로운 배포 포트 결정
    if [ "$CURRENT_PORT" == "$BLUE_PORT" ]; then
        NEW_PORT=$GREEN_PORT
        NEW_COLOR="GREEN"
        OLD_COLOR="BLUE"
    else
        NEW_PORT=$BLUE_PORT
        NEW_COLOR="BLUE"
        OLD_COLOR="GREEN"
    fi
fi

echo "Deploying to $NEW_COLOR ($NEW_PORT)..."

# 이전 컨테이너 먼저 정리 (메모리 확보)
OLD_CONTAINER="${CONTAINER_NAME}-${NEW_COLOR}"
if docker ps -a --format '{{.Names}}' | grep -q "^${OLD_CONTAINER}$"; then
    echo "Removing old container: $OLD_CONTAINER"
    docker stop $OLD_CONTAINER || true
    docker rm $OLD_CONTAINER || true
fi

# 새 컨테이너 배포
export IMAGE_FULL_URL=$IMAGE_URL
export DOCKERHUB_IMAGE_NAME="${CONTAINER_NAME}-${NEW_COLOR}"
export SERVER_PORT=$NEW_PORT

cd ~/app
docker compose -f docker/docker-compose-dev.yml up -d

# 헬스체크 (최대 3분)
echo "Health checking on port $NEW_PORT..."
for i in {1..90}; do
    if curl -f http://localhost:$NEW_PORT$HEALTH_CHECK_PATH > /dev/null 2>&1; then
        echo "Health check passed!"
        break
    fi
    if [ $i -eq 90 ]; then
        echo "Health check failed after 90 attempts (3 minutes)"
        docker compose -f docker/docker-compose-dev.yml down
        exit 1
    fi
    echo "Waiting for application to be ready... ($i/90)"
    sleep 2
done

# Nginx 포트 스위칭
echo "Switching Nginx to port $NEW_PORT..."
sudo sed -i "s/server 127.0.0.1:[0-9]\+;/server 127.0.0.1:$NEW_PORT;/" $NGINX_CONF
sudo nginx -t && sudo systemctl reload nginx

echo "Deployment successful! Active: $NEW_COLOR ($NEW_PORT)"

# 이전 색상 컨테이너 정리 (Graceful shutdown)
if [ -n "$OLD_COLOR" ]; then
    OLD_CONTAINER="${CONTAINER_NAME}-${OLD_COLOR}"
    if docker ps -a --format '{{.Names}}' | grep -q "^${OLD_CONTAINER}$"; then
        echo "Waiting 30 seconds for connection draining..."
        sleep 30
        echo "Gracefully stopping previous container: $OLD_CONTAINER"
        docker stop -t 30 $OLD_CONTAINER || true
        docker rm $OLD_CONTAINER || true
    fi
fi

docker image prune -a -f
echo "Blue/Green deployment completed!"
