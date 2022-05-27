package net.clotfelter.duncan.ShoppingCartDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ShoppingCartDemoApplication {
	static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SpringApplication.run(ShoppingCartDemoApplication.class, args);

		var sessionFactory = HibernateAnnotationUtil.getSessionFactory();
		var session = sessionFactory.getCurrentSession();
		System.out.println("Session created");

		var tx = session.beginTransaction();

		session.saveOrUpdate(context.getBean("spiderman"));
		session.saveOrUpdate(context.getBean("beeman"));
		session.saveOrUpdate(context.getBean("batman"));

		tx.commit();
	}

	@Bean
	public static JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("gensparkmovies@gmail.com");
		mailSender.setPassword("password123genspark");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}
