#!/bin/bash

PORT=7080
CONTAINER_NAME="taki"
DOCKER_IMAGE="jhshadi/$CONTAINER_NAME"

echo "Cleaning old '$CONTAINER_NAME' container"
docker stop home-control
docker rm home-control

echo "Building '$DOCKER_IMAGE' image"
docker build -t ${DOCKER_IMAGE} .

echo "Starting '$CONTAINER_NAME' container"
docker run -d --net=host -e PORT="$PORT" --name=${CONTAINER_NAME} ${DOCKER_IMAGE}