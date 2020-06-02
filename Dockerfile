FROM harbor.examecorp.local/library/maven:3.6.3-jdk-11

EXPOSE 8080
WORKDIR /usr/src/main/java
ADD . /usr/src/main/java
RUN mvn clean test



