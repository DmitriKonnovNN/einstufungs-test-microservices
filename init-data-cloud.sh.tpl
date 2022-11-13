#!/bin/bash
echo "-----------------------SCRIPT STARTS------------------------------"
echo "running droplet ${instance_name}"
apt update -y
apt install -y net-tools
apt install -y docker.io
usermod -aG docker ubuntu
echo "-----------------install docker compose----------------------------"
curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
docker-compose --version
echo "-----------------------SCRIPT STOPS-------------------------------"