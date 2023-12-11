#!/bin/bash

#jps -l | grep -v 66065 | grep ".jar" | awk '{print $1}' | xargs kill
# В script1.sh и script2.sh добавьте следующую строку в начале файла:
#echo "Current directory: $(pwd)"
#echo "files : $(ls)"

source ./run_killer.sh

java -jar dispatcher/target/dispatcher-1.0-SNAPSHOT.jar &
java -jar node/target/node-1.0-SNAPSHOT.jar &
java -jar node-order-service/target/node-order-service-0.0.1-SNAPSHOT.jar &
java -jar node-check-order-service/target/node-check-order-service-0.0.1-SNAPSHOT.jar &
java -jar node-start-service/target/node-start-service-0.0.1-SNAPSHOT.jar &


# Добавьте любое количество JAR-файлов и их запусков в нужной последовательности
