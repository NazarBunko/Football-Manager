# Візьми офіційний образ з OpenJDK 17 для запуску
FROM eclipse-temurin:17-jdk-jammy

# Копіюємо готовий jar в контейнер
COPY target/Football-Manager-0.0.1-SNAPSHOT.jar app.jar

# Вказуємо команду запуску
ENTRYPOINT ["java","-jar","/app.jar"]
