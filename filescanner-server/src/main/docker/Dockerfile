FROM jeanblanchard/busybox-java:8
MAINTAINER lm "mueller.lutz@gmail.com"

ADD *.jar app.jar
RUN touch /app.jar
CMD mkdir /media
CMD mkdir /filescanner
ENTRYPOINT ["java","-jar","-Dfilescanner.path.contentroot=file:///media","-Dfilescanner.path.home=/filescanner","/app.jar","--logging.file=/filescanner.log"]