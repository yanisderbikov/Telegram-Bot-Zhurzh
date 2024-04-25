#!/bin/bash

java -jar node-1.2.3.jar &
java -jar node-order-service-1.2.3.jar &
java -jar node-check-order-service-1.2.3.jar &
java -jar node-faq-service-1.2.3.jar &
java -jar node-start-service-1.2.3.jar &
java -jar node-price-list-1.2.3.jar &

wait
