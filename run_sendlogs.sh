#!/bin/bash

# Данные для подключения к серверу
server_user="yan"
server_host="89.77.170.178"
server_port="22"
server_logs_path="/home/yan/"
local_destination_path="/Users/yanderbikovmail.ru/Documents/folder/marzipan"

# Список файлов на сервере
files_on_server=(
#  "dispatcher.log"
#  "node-check-order-service.log"
#  "node-order-service.log"
#  "node-start-service.log"
#  "groupnode.log"
#  "node-faq-service.log"
#  "node-price-list.log"
#  "node.log"
  "dumb_28_03_2024.sql"

)

# Получение файлов с сервера
for file in "${files_on_server[@]}"; do
    scp -P "$server_port" "$server_user@$server_host:$server_logs_path/$file" "$local_destination_path"
done
