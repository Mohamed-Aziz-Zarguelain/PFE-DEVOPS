FROM openjdk:17
EXPOSE 8087
ADD target/backendbeetrack-0.0.1-SNAPSHOT.jar backendbeetrack-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/backendbeetrack-0.0.1-SNAPSHOT.jar"]