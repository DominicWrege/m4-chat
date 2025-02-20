FROM docker.io/maven:3-ibm-semeru-21-jammy as builder

WORKDIR /build

COPY . .

RUN mvn clean package

FROM docker.io/tomcat:9.0.100-jre21-temurin-noble

COPY --from=builder /build/target/m4-chat-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

WORKDIR /usr/local/tomcat

EXPOSE 8080

ENV OPENAI_API_KEY=""
ENV DATABASE_FILE="/tmp/database.db"
