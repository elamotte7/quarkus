####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Before building the docker image run:
#
# mvn package
#
# Then, build the image with:
#
# docker build -f src/main/docker/Dockerfile.jvm -t quarkus/backend-jvm .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 quarkus/backend-jvm
#
###
FROM fabric8/java-centos-openjdk11-jre

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV AB_ENABLED=jmx_exporter

COPY ./lib/* /deployments/lib/
COPY ./*-runner.jar /deployments/app.jar
COPY ./docker-entrypoint.sh /root/docker-entrypoint.sh

USER root

RUN chmod +x /root/docker-entrypoint.sh

ENTRYPOINT [ \
	"/root/docker-entrypoint.sh" \
]
