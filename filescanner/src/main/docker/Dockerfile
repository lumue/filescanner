FROM jeanblanchard/busybox-java:8
MAINTAINER lm "mueller.lutz@gmail.com"

ADD filescanner-server-0.0.1-SNAPSHOT.jar app.jar
RUN touch /app.jar
CMD mkdir /media
CMD mkdir /filescanner
ENTRYPOINT ["java","-jar","/app.jar","-D","spring.data.elasticsearch.clusterNodes=192.168.1.105:9301","-D","logging.file=/filescanner/filescanner.log"]