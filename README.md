Для запуска приложения 
необходимо в Maven написать следующую
функцию  
`mvn clean install`

Для запуска приложения достаточно запустить
контейнеры docker RabbitMQ и PostgreSQL.

Jar файлы будут находиться в кажом модуле. 
Далее запускаем Dispatcher.jar и Node.jar


----
Быстрый запуск программы  
`mvn clean install`  
`mvn package`

Затем запустите файл `run_services.sh`

