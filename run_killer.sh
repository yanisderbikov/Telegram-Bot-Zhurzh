#!/bin/bash

jps -l | grep -v 48671 | grep ".jar" | awk '{print $1}' | xargs kill



# Добавьте любое количество JAR-файлов и их запусков в нужной последовательности
