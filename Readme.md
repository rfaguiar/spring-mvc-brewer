[![Project on GitScrum](https://gitscrum.com/badges/project.svg?project=rfaguiar/springmvc-app1)](https://gitscrum.com/projects/rfaguiar/springmvc-app1)
[![Build Status](https://travis-ci.org/rfaguiar/spring-mvc-brewer.svg?branch=master)](https://travis-ci.org/rfaguiar/spring-mvc-brewer) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.brewer%3Abrewer&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.brewer%3Abrewer) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.brewer%3Abrewer&metric=coverage)](https://sonarcloud.io/component_measures?id=com.brewer%3Abrewer&metric=coverage)  

# Tecnologias: SpringMVC, Spring Data, Spring Security, Thymeleaf 3, Java 8, Tomcat 8, Hibernate Validator, Maven 3, MySql 5, log4j  

* compilação com maven 3.3  
    > mvn clean package -Plocal-jndi  
    > mvn clean package -Plocal  
    > mvn clean package -Pprod  
* webrunner  
    > java $JAVA_OPTS -Dspring.profiles.active=prod -jar target/dependency/webapp-runner.jar target/brewer.war --contex-xml target/brewer/META-INF/context.xml
* serviço para envio de email  
    > https://sendgrid.com/  
* variaveis de ambiente de producao  
    > JAWSDB_URL  
    > SEND_GRID_USERNAME  
    > SEND_GRID_PASSWORD  
    > BUCKET_NAME  
    > AWS_ACESS_KEY_ID  
    > AWS_SECRET_KEY_ID  

* flyway-migrações do bd  
    > mvn -Dflyway.user=root -Dflyway.password=root -Dflyway.url=jdbc:mysql://localhost:3306/brewer?useSSL=false flyway:migrate  

#### Aplicação na cloud do heroku pode estar dormindo, tenha paciência  
    
[link para aplicação no heroku!](https://brewer-springmvc-app1.herokuapp.com)  
    
user: admin@brewer.com  
pass: admin  
