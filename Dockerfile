FROM adoptopenjdk/openjdk11:alpine-jre
ADD web/build/libs/springApp.war application.war
CMD java -jar application.war