* webrunner
    > java -jar target/dependency/webappp-runner.jar target/brewer.war --contex-xml target/brewer/META-INF/context.xml

* flyway-migrações do bd
    > mvn -Dflyway.user=root -Dflyway.password=root -Dflyway.url=jdbc:mysql://localhost:3306/brewer?useSSL=false flyway:migrate
* acesso aplicação:
    > user: admin@brewer.com
    > pass: admin