FROM harbor.vip56.cn/common/openjdk:8-jdk-alpine-with-fonts

VOLUME /tmp

COPY ./target/*.jar app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=80"]