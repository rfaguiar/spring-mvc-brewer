package com.brewer.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.brewer.brewer.mail.Mailer;

@Configuration
@ComponentScan(basePackageClasses = Mailer.class)
@PropertySource({"classpath:env/mail-${ambiente:local}.properties"})
@PropertySource(value = { "file:\\C:\\opt\\.brewer-mail.properties" }, ignoreResourceNotFound = true)
public class MailConfig {
	
	@Autowired
	private Environment env;

	@Bean
	@Profile("local")
	public JavaMailSender mailSenderLocal(){
        return createJavaMailSender("email.username", "password");
	}

	@Bean
	@Profile("prod")
	public JavaMailSender mailSenderProducao(){
		return createJavaMailSender("email.username", "SEND_GRID_PASSWORD");
	}

	private JavaMailSenderImpl createJavaMailSender(String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.sendgrid.net");
        mailSender.setPort(587);
        mailSender.setUsername(env.getProperty(username));
        mailSender.setPassword(env.getProperty(password));

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.debug", false);
        props.put("mail.smtp.connectiontimeout", 10000);//milisegundos

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}
