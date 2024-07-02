#!/usr/bin/env bash
# 切换到项目根目录
cd "$(git rev-parse --show-toplevel)"

docker buildx build --platform=linux/amd64,linux/arm64 -t huajuan6848/ase_user_service:yys-lab4 -f ase_user_service/Dockerfile --push .
docker buildx build --platform=linux/amd64,linux/arm64 -t huajuan6848/ase_notification_service:yys-lab4 -f ase_notification_service/Dockerfile --push .
docker buildx build --platform=linux/amd64,linux/arm64 -t huajuan6848/ase_file_service:yys-lab4 -f ase_file_service/Dockerfile --push .
docker buildx build --platform=linux/amd64,linux/arm64 -t huajuan6848/ase_conference_service:yys-lab4 -f ase_conference_service/Dockerfile --push .
docker buildx build --platform=linux/amd64,linux/arm64 -t huajuan6848/ase_contribute_service:yys-lab4 -f ase_contribute_service/Dockerfile --push .
docker buildx build --platform=linux/amd64,linux/arm64 -t huajuan6848/ase_review_service:yys-lab4 -f ase_review_service/Dockerfile --push .

cd ase_frontend && docker buildx build --platform=linux/amd64,linux/arm64 -t huajuan6848/ase_frontend:yys-lab4 -f Dockerfile --push .

cd -