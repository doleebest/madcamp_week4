#FROM openjdk:21-jdk
#
#WORKDIR /app
#
#COPY build/libs/Our-Beloved-KAIST-0.0.1-SNAPSHOT.jar /app.jar
#COPY firebaseServiceAccountKey.json /app/firebaseServiceAccountKey.json
#
#EXPOSE 8080
#
## 헬스체크 타임아웃 증가 및 메모리 설정 추가
#ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM openjdk:21-jdk-slim
#WORKDIR /app
#COPY build/libs/*.jar app.jar
#
#ENV PORT=8080
#EXPOSE ${PORT}
#
#CMD ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]

FROM --platform=linux/amd64 openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=${PORT}"]