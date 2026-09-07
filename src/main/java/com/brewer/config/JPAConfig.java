package com.brewer.config;

import com.brewer.model.Cerveja;
import com.brewer.repository.Cervejas;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackageClasses = Cervejas.class)
@EnableJpaRepositories(basePackageClasses = Cervejas.class, enableDefaultTransactions = false)
@EnableTransactionManagement
public class JPAConfig {

    @Bean
	@Profile("local-jndi")
	public DataSource dataSourceLocalJndi(){
        JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
        jndiDataSourceLookup.setResourceRef(true);
		return jndiDataSourceLookup.getDataSource("jdbc/brewerDB");
	}

	@Bean
	@Profile("local")
	public DataSource dataSourceLocal() throws SQLException, ClassNotFoundException {
        return createBasicDatasource("jdbc:mysql://localhost:3306/brewer?useSSL=false", "root", "root");

	}

	@Bean
	@Profile("docker-desenv")
	public DataSource dataSourceDocker() throws SQLException, ClassNotFoundException {
    	String url = System.getenv("JDBC_URL");
    	String username = System.getenv("JDBC_USER");
    	String password = System.getenv("JDBC_PASS");
        BasicDataSource datasource = createBasicDatasource(url, username, password);
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        return datasource;

	}

    @Bean
    @Profile("prod")
    public DataSource dataSourceProducao() throws URISyntaxException, SQLException, ClassNotFoundException {
        URI jdbUri = new URI(System.getenv("JAWSDB_URL"));

        String username = jdbUri.getUserInfo().split(":")[0];
        String password = jdbUri.getUserInfo().split(":")[1];
        String port = String.valueOf(jdbUri.getPort());
        String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();
        return createBasicDatasource(jdbUrl, username, password);
    }
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter(){
		HibernateJpaVendorAdapter adaptor = new HibernateJpaVendorAdapter();
		adaptor.setDatabase(Database.MYSQL);
		adaptor.setShowSql(false);
		adaptor.setGenerateDdl(false);
		adaptor.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		return adaptor;
		
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) throws Exception {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setPackagesToScan(Cerveja.class.getPackage().getName());
		factory.setMappingResources("sql/consultas-nativas.xml");
		factory.afterPropertiesSet();

		return factory.getObject();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	private BasicDataSource createBasicDatasource(String jdbUrl, String username, String password) throws SQLException, ClassNotFoundException {
	    BasicDataSource datasource = new BasicDataSource();
	    datasource.setUrl(jdbUrl);
	    datasource.setUsername(username);
	    datasource.setPassword(password);
	    datasource.setInitialSize(9);
	    datasource.setMaxTotal(9);
	    datasource.setDriverClassName("com.mysql.jdbc.Driver");
	    Class.forName("com.mysql.jdbc.Driver");
	    return datasource;
	}
}
