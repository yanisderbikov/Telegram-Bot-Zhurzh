#!/bin/bash

# Данные для подключения к серверу
server_user="yan"
server_host="89.77.170.178"
server_port="22"
server_path="/mnt/my_apps/zhurzhbot"
local_project_path="/Users/yanderbikovmail.ru/Documents/ProjectsIDE/Telegram/Telegram-Bot-Zhurzh"

# Перечень файлов для отправки
files_to_send=(
    "docker-compose.yml"
#    "Dockerfile-all"
#    "start-apps.sh"
#    "Dockerfile"
#    ".env"
)

# Отправка файлов
for file in "${files_to_send[@]}"; do
    scp -P "$server_port" "$local_project_path/$file" "$server_user@$server_host:$server_path"
done
