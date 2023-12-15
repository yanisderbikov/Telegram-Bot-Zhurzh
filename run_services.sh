#!/bin/bash

source ./run_killer.sh

# dispatcher
java -jar dispatcher/target/dispatcher-1.0-SNAPSHOT.jar &
# start
java -jar node-start-service/target/node-start-service-0.0.1-SNAPSHOT.jar &

# node
java -jar node/target/node-1.0-SNAPSHOT.jar &

# order
java -jar node-order-service/target/node-order-service-0.0.1-SNAPSHOT.jar &

# check order
java -jar node-check-order-service/target/node-check-order-service-0.0.1-SNAPSHOT.jar &

# price list
java -jar node-price-list/target/node-price-list-0.0.1-SNAPSHOT.jar &

# FAQ
java -jar node-faq-service/target/node-faq-service-0.0.1-SNAPSHOT.jar &


# Добавьте любое количество JAR-файлов и их запусков в нужной последовательности
