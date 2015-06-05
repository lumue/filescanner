FROM jeanblanchard/busybox-java:8
MAINTAINER lm "mueller.lutz@gmail.com"

ADD filescanner-0.0.1-SNAPSHOT.jar app.jar
RUN touch /app.jar
CMD mkdir /media
CMD mkdir /filescanner
ENTRYPOINT ["java","-jar","/app.jar","-D","filescanner.path.root=/media","-D","logging.file=/filescanner/filescanner.log"]