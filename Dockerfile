# ---- Build Stage ----

#Maven və Java 17 hazır olan image istifadə et,"AS build" isə bu mərhələyə ad verir:build
FROM maven:3.9-eclipse-temurin-21 AS build

#Container daxilində iş qovluğu yaradır:/app ,Sonrakı əmrlər bu qovluqda işləyir.
WORKDIR /app

# Əvvəlcə yalnız pom.xml kopyala ki, dependency-lər cache-lənsin
# pom.xml faylını Docker container-in içindəki: /app/pom.xml yerinə kopyalayır.
COPY pom.xml .

# Bu əmr pom.xml-ə baxır və dependency-ləri yükləyir.(Məsələn: Spring Boot, Hibernate, PostgreSQL)
RUN mvn dependency:go-offline -B

# İndi bütün source kodu kopyala və build et
# indi lokal layihəmdəki src qovluğu dockerə kopyalanır-(/app/src)
COPY src ./src

# RUN mvn: Bu əmr Maven build edir,Nəticədə:target/excermol-0.0.1-SNAPSHOT.jar-kimi JAR yaranır
# -DskipTests : Testləri run etmə, JAR-ı yarat
# -B : batch mode deməkdir və Maven-in interaktiv suallar verməsinin qarşısını alır
RUN mvn clean package -DskipTests -B


# ---- Runtime Stage ----

# ikinci image-Burada artıq: Maven yoxdur JDK yoxdur,Sadəcə: Java 21,JRE
# Çünki application-ı build etmək artıq bitib,Bizə indi sadəcə JAR-ı işlətmək lazımdır.
# Multi-stage build-in əsas üstünlüyü-Build image: Maven,JDK,Dependencies,Source code,JAR,amma Runtime imagedə: JRE,JAR Yəni runtime image daha kiçik və daha təmiz olur.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Build stage-dən yalnız jar faylını götür
# build adlı əvvəlki mərhələdən /app/target/ içindəki JAR faylını götür və hazırkı image-a app.jar adı ilə kopyala.(/app/app.jar)
COPY --from=build /app/target/*.jar app.jar

# Bu Docker-ə bildirir:"Bu application container daxilində 8080 portunda işləyəcək."
EXPOSE 8080

# container başladıqda avtomatik bu əmr başladılır yəni:docker container start--->java -jar app.jar--->Spring Boot application başlayır
ENTRYPOINT ["java", "-jar", "app.jar"]