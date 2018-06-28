FROM jboss/base-jdk:8
MAINTAINER lm "mueller.lutz@gmail.com"

CMD locale-gen de_DE.UTF-8 && update-locale LANG=de_DE.UTF-8


ADD *.jar app.jar
CMD mkdir /media
CMD mkdir /filescanner
ENTRYPOINT ["java","-jar","-Dspring.data.mongodb.host=mongo","-Dfilescanner.path.contentroot=file:///media","-Dfilescanner.path.home=/filescanner","-Dfilescanner.properties=/filescanner/config/filescanner.properties","/app.jar"]