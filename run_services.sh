#!/bin/bash

jps -l | grep -v 66065 | grep ".jar" | awk '{print $1}' | xargs kill


java -jar dispatcher/target/dispatcher-1.0-SNAPSHOT.jar &
java -jar node/target/node-1.0-SNAPSHOT.jar &
java -jar node-order-service/target/node-order-service-0.0.1-SNAPSHOT.jar &
java -jar node-start-service/target/node-start-service-0.0.1-SNAPSHOT.jar &


# Добавьте любое количество JAR-файлов и их запусков в нужной последовательности
