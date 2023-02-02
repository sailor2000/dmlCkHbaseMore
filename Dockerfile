FROM hub.c.163.com/dwyane/openjdk:8
VOLUME /tmp
ADD target/clickHouse-1.0.0.jar app.jar
EXPOSE 9001 9002 9003
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#ENTRYPOINT ["java","-version"]
