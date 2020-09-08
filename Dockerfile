FROM adoptopenjdk/openjdk14:alpine-slim
RUN apk add tzdata
RUN ln -s /usr/share/zoneinfo/America/Bogota /etc/localtime
RUN mkdir /security-starter
RUN mkdir /security-starter/logs
RUN mkdir /security-starter/runnable
COPY target/securitystarter-0.0.1-SNAPSHOT.jar /security-starter/runnable/security-starter.jar
EXPOSE 8080
CMD ["java", "-jar", "/security-starter/runnable/security-starter.jar"]