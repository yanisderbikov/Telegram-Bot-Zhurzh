#!/bin/bash

source ./run_killer.sh

pid_file="pids.txt"

# Очистка файла с PID перед добавлением новых данных
echo -n > "$pid_file"
# dispatcher
java -jar dispatcher/target/dispatcher-1.2.0.jar &
echo "dispatcher PID: $!" >> "$pid_file"

# start
java -jar node-start-service/target/node-start-service-1.2.0.jar &
echo "start PID: $!" >> "$pid_file"

# node
java -jar node/target/node-1.2.0.jar &
echo "node PID: $!" >> "$pid_file"

# order
java -jar node-order-service/target/node-order-service-1.2.0.jar &
echo "order PID: $!" >> "$pid_file"

# check order
java -jar node-check-order-service/target/node-check-order-service-1.2.0.jar &
echo "check order PID: $!" >> "$pid_file"

# price list
java -jar node-price-list/target/node-price-list-1.2.0.jar &
echo "price list PID: $!" >> "$pid_file"

# FAQ
java -jar node-faq-service/target/node-faq-service-1.2.0.jar &
echo "FAQ PID: $!" >> "$pid_file"

# group node
java -jar group-node/target/group-node-1.2.0.jar &
echo "group node PID: $!" >> "$pid_file"