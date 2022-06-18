package com.alison.lojadelivros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@EntityScan(basePackages = "com.alison.lojadelivros.model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.alison.lojadelivros.repository"})
@EnableTransactionManagement
@Configuration
@EnableWebMvc
public class LojadelivrosApplication extends WebMvcConfigurerAdapter implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(LojadelivrosApplication.class, args);
		
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("/login");
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);
	}

	@Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 registry.addResourceHandler("/webjars/**", "/resources/**", "/static/**", "/assets/**", "/img/**", "/css/**", "/js/**",
					"classpath:/static/", "classpath:/resources/")
			.addResourceLocations("/webjars/", "/resources/",
							"classpath:/static/**", "classpath:/static/img/**", "classpath:/static/", "classpath:/static/assets/",
							"classpath:/resources/", "classpath:/static/css/", "classpath:/static/js/", "/resources/**",
							"/WEB-INF/classes/static/**");
	}

}
