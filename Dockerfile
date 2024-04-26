#FROM openjdk:11
#ADD target/immoben-backend.jar immoben-backend.jar
#ENTRYPOINT ["java","-jar","/immoben-backend.jar"]


FROM maven:3.9.2-eclipse-temurin-17-alpine as builder

COPY ./src src/
COPY ./pom.xml pom.xml
#RUN npm install
#RUN npm run build
#RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
#COPY --from=builder target/*.jar immoben-backend.jar
#COPY --from=builder target/*.jar immoben-backend.jar
#COPY --from=builder target/*.jar app.jar
EXPOSE 8888
CMD ["java","-jar","/immoben-backend.jar"]