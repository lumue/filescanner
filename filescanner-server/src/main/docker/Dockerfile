FROM jboss/base-jdk:8
MAINTAINER lm "mueller.lutz@gmail.com"

ADD *.jar /app.jar
RUN locale-gen de_DE.UTF-8 && echo LANG=de_DE.UTF-8 > /etc/default/locale  && chmod a+rwx /app.jar && mkdir -p /mnt/nasbox/media && mkdir /filescanner && chmod a+rwx /filescanner

ENTRYPOINT ["java","-jar","-Dspring.data.mongodb.host=mongo","-Dfilescanner.path.contentroot=file:///mnt/nasbox/media","-Dfilescanner.path.home=/filescanner","-Dfilescanner.properties=/filescanner/config/filescanner.properties","/app.jar"]