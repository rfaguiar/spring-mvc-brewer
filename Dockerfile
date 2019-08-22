#FROM maven:3.3-jdk-8 as build
#COPY . ./app
#WORKDIR /app
#VOLUME "$USER_HOME_DIR/.m2"
#RUN mvn package

FROM tomcat:8.0-jre8
MAINTAINER Rogerio Aguiar < rfaguiar1@gmail.com>
ADD tomcat8/tomcat-users.xml  $CATALINA_HOME/conf/
RUN rm -Rf $CATALINA_HOME/webapps/*
COPY ./target/*.war $CATALINA_HOME/webapps/ROOT.war

VOLUME $CATALINA_HOME/webapps

#habilita remote debug na porta 8000
ENV JPDA_ADDRESS="8000"
ENV JPDA_TRANSPORT="dt_socket"

EXPOSE 8080 8000
ENTRYPOINT ["catalina.sh", "jpda", "run"]
