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

@Configuration
@ComponentScan(basePackageClasses = Cervejas.class)
@EnableJpaRepositories(basePackageClasses = Cervejas.class, enableDefaultTransactions = false)
@EnableTransactionManagement
public class JPAConfig {

    private JndiDataSourceLookup jndiDataSourceLookup;
    private BasicDataSource basicDataSource;
    private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    public JPAConfig() {
        this(new JndiDataSourceLookup(), new BasicDataSource(), new LocalContainerEntityManagerFactoryBean());
    }

    public JPAConfig(JndiDataSourceLookup jndiDataSourceLookup,
                     BasicDataSource basicDataSource,
                     LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        this.jndiDataSourceLookup = jndiDataSourceLookup;
        this.basicDataSource = basicDataSource;
        this.localContainerEntityManagerFactoryBean = localContainerEntityManagerFactoryBean;
    }

    @Bean
	@Profile("local-jndi")
	public DataSource dataSourceLocalJndi(){
        jndiDataSourceLookup.setResourceRef(true);
		return jndiDataSourceLookup.getDataSource("jdbc/brewerDB");
	}

	@Bean
	@Profile("local")
	public DataSource dataSourceLocal(){
        return createBasicDatasource("jdbc:mysql://localhost:3306/brewer?useSSL=false", "root", "root");

	}

    @Bean
    @Profile("prod")
    public DataSource dataSourceProducao() throws URISyntaxException {
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
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter){
		localContainerEntityManagerFactoryBean.setDataSource(dataSource);
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		localContainerEntityManagerFactoryBean.setPackagesToScan(Cerveja.class.getPackage().getName());
		localContainerEntityManagerFactoryBean.setMappingResources("sql/consultas-nativas.xml");
		localContainerEntityManagerFactoryBean.afterPropertiesSet();
		
		return localContainerEntityManagerFactoryBean.getObject();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	private BasicDataSource createBasicDatasource(String jdbUrl, String username, String password) {
        basicDataSource.setUrl(jdbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setInitialSize(9);
        basicDataSource.setMaxTotal(9);
        return basicDataSource;
    }
}
