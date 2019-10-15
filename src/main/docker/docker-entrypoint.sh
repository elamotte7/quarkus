#!/bin/sh
JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager ${JAVA_OPTIONS:-}"

echo ${JAVA_OPTIONS}

sleep ${START_DELAY:-0}

# for more options see: https://github.com/fabric8io-images/run-java-sh/blob/master/fish-pepper/run-java-sh/readme.md
./deployments/run-java.sh
