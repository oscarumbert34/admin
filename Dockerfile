FROM openjdk:8

EXPOSE 8091

ADD target/admin-core-0.0.1-SNAPSHOT.jar admin-core-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/admin-core-0.0.1-SNAPSHOT.jar"]