FROM maven:3.8.1-jdk-11-slim AS c2d431f3
WORKDIR /project
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:11-jre
RUN mkdir -p /opt/logging \ && mkdir -p /opt/supplier-service/config
COPY --from=c2d431f3 /project/target/supplier-service.jar /opt/supplier-service/supplier-service.jar
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-DCONSUL_HOST=172.17.0.2", "-DCONSUL_PORT=8500", "-DDB_HOST=172.17.0.3", "-DDB_PORT=3306", "-DDB_USER=root", "-DDB_PASS=restaurant-inventory-rnd.tk", "-Dfile.encoding=utf-8", "-Dsun.jnu.encoding=utf-8", "-jar", "/opt/supplier-service/supplier-service.jar" ]