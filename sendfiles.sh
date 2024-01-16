#!/bin/bash

# Директория, куда будут отправляться файлы JAR на сервере
REMOTE_DIR="/home/yan/IdeaProjects/zhurzh"

# Список файлов JAR для отправки
# Пример: JARS=("file1.jar" "file2.jar" "file3.jar")
JARS=("dispatcher/target/dispatcher-1.0-SNAPSHOT.jar"
"node-start-service/target/node-start-service-0.0.1-SNAPSHOT.jar"
"node/target/node-1.0-SNAPSHOT.jar"
"node-order-service/target/node-order-service-0.0.1-SNAPSHOT.jar"
"node-check-order-service/target/node-check-order-service-0.0.1-SNAPSHOT.jar"
"node-price-list/target/node-price-list-0.0.1-SNAPSHOT.jar"
"node-faq-service/target/node-faq-service-0.0.1-SNAPSHOT.jar"
"group-node/target/group-node-0.0.1-SNAPSHOT.jar"
"run.sh")

# SSH детали
SSH_HOST="yan@4.tcp.eu.ngrok.io"
SSH_PORT=12603

# Отправка файлов на сервер
for jar in "${JARS[@]}"
do
  echo "Отправка $jar на сервер..."
  scp -P $SSH_PORT "$jar" "${SSH_HOST}:${REMOTE_DIR}/"
  echo "$jar отправлен."
done

echo "Все файлы отправлены."
