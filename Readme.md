[![Build Status](https://travis-ci.org/rfaguiar/spring-mvc-brewer.svg?branch=master)](https://travis-ci.org/rfaguiar/spring-mvc-brewer) [![Quality Gate](https://sonarcloud.io/dashboard?id=com.brewer%3Abrewer)](https://sonarcloud.io/api/project_badges/measure?project=com.brewer%3Abrewer&metric=alert_status) [![Coverage](https://sonarcloud.io/component_measures?id=com.brewer%3Abrewer&metric=coverage)](https://sonarcloud.io/api/project_badges/measure?project=com.brewer%3Abrewer&metric=coverage)

* webrunner
    > java -jar target/dependency/webappp-runner.jar target/brewer.war --contex-xml target/brewer/META-INF/context.xml

* flyway-migrações do bd
    > mvn -Dflyway.user=root -Dflyway.password=root -Dflyway.url=jdbc:mysql://localhost:3306/brewer?useSSL=false flyway:migrate
* acesso aplicação:
    > user: admin@brewer.com
    > pass: admin
