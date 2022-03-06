FROM maven:3.8.1-jdk-11-slim AS c2d431f3
WORKDIR /project
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:11-jre
RUN mkdir -p /opt/logging \ && mkdir -p /opt/api-security/config
COPY --from=c2d431f3 /project/target/api-security.jar /opt/api-security/api-security.jar
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-DCONSUL_HOST=172.17.0.2", "-DDB_HOST=172.17.0.4", "-Dfile.encoding=utf-8", "-Dsun.jnu.encoding=utf-8", "-jar", "/opt/api-security/api-security.jar" ]