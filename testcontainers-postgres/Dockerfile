FROM openjdk:19-jdk-alpine3.15
VOLUME /tmp
MAINTAINER Denis Rosa <denis.rosa@couchbase.com>
ARG JAR_FILE=target/testcontainer-wallet-microservice-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.datasource.url=${DATASOURCE_URL},spring.datasource.password=${DATASOURCE_PASSWORD},spring.datasource.username=${DATASOURCE_USERNAME}","-jar","/app.jar"]