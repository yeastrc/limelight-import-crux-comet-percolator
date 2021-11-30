FROM amazoncorretto:11-alpine-jdk

COPY build/libs/cruxCometPercolator2LimelightXML.jar  /usr/local/bin/cruxCometPercolator2LimelightXML.jar

ENTRYPOINT ["java", "-jar", "/usr/local/bin/cruxCometPercolator2LimelightXML.jar"]