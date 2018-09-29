FROM lumue/jdkydl:alpine
MAINTAINER lm "mueller.lutz@gmail.com"

ADD *.jar /app.jar


CMD chmod a+rwx /app.jar && mkdir -p /mnt/nasbox/media &&  mkdir /filescanner && chmod a+rwx /filescanner

EXPOSE 8002

VOLUME ["/filescanner"]
VOLUME ["/mnt/nasbox/media"]

ENTRYPOINT ["java","-jar","-Dspring.data.mongodb.host=mongo","-Dfilescanner.path.contentroot=file:///mnt/nasbox/media","-Dfilescanner.path.home=/filescanner","/app.jar","--spring.config.additional-location=file:/filescanner/config/filescanner.properties"]