# Используйте официальный образ Java 17
FROM openjdk:17

# Определите аргумент для имени JAR-файла
ARG JAR_FILE

# Установите рабочую директорию внутри контейнера
WORKDIR /app

# Копируйте jar файл вашего приложения в рабочую директорию
COPY ${JAR_FILE} /app/my-app.jar

# Укажите команду для запуска вашего приложения
CMD ["java", "-jar", "my-app.jar"]
