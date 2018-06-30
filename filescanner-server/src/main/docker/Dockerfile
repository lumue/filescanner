FROM jboss/base-jdk:8
MAINTAINER lm "mueller.lutz@gmail.com"

ADD *.jar /app.jar
USER root

RUN chmod a+rwx /app.jar && mkdir -p /mnt/nasbox/media && chown -R jboss:jboss /mnt/nasbox/media && mkdir /filescanner && chmod a+rwx /filescanner && chown -R jboss:jboss /filescanner
RUN localedef -v -c -i de_DE -f UTF-8 de_DE.UTF-8

ENTRYPOINT ["java","-jar","-Dspring.data.mongodb.host=mongo","-Dfilescanner.path.contentroot=file:///mnt/nasbox/media","-Dfilescanner.path.home=/filescanner","-Dfilescanner.properties=/filescanner/config/filescanner.properties","/app.jar"]