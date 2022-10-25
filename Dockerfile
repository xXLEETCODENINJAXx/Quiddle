FROM openjdk:11
MAINTAINER emmatblingx@gmail.com
WORKDIR srv/app
COPY build/libs/school-service-app-0.0.1-SNAPSHOT.jar school-service-app-0.0.1-SNAPSHOT.jar
#COPY out/artifacts/school_service_app_main_jar/school-service-app.main.jar school-service-app-0.0.1-SNAPSHOT.jar
#COPY out/production/resources .
ENTRYPOINT ["java","-jar","/srv/app/school-service-app-0.0.1-SNAPSHOT.jar"]