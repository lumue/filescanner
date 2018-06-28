FROM jeanblanchard/busybox-java:8
MAINTAINER lm "mueller.lutz@gmail.com"

ADD *.jar app.jar
RUN touch /app.jar
CMD mkdir /media
CMD mkdir /filescanner
ENTRYPOINT ["java","-jar","-Dspring.data.mongodb.password=filescanner","-Dspring.data.mongodb.username=filescanner","-Dspring.data.mongodb.host=mongo","-Dspring.data.mongodb.port=27018","-Dfilescanner.path.contentroot=file:///media","-Dfilescanner.path.home=/filescanner","/app.jar","--logging.file=/filescanner.log"]