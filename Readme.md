[![Build Status](https://travis-ci.org/rfaguiar/spring-mvc-brewer.svg?branch=master)](https://travis-ci.org/rfaguiar/spring-mvc-brewer)![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.brewer%3Abrewer&metric=alert_status)

* compilação com maven 3.3:
    > mvn clean package -Plocal-jndi
    > mvn clean package -Plocal
    > mvn clean package -Pprod

* webrunner
    > java -jar target/dependency/webappp-runner.jar target/brewer.war --contex-xml target/brewer/META-INF/context.xml

* ENVIRONMENTS:
    > JAWSDB_URL
    > SEND_GRID_PASSWORD

* flyway-migrações do bd
    > mvn -Dflyway.user=root -Dflyway.password=root -Dflyway.url=jdbc:mysql://localhost:3306/brewer?useSSL=false flyway:migrate
* acesso aplicação:
    > user: admin@brewer.com
    > pass: admin