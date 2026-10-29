package com.backend;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	/*
	 * configure ModelMapper as spring bean
	 * <bean id class ...../>
	 * Add @Bean annotated method to return ModelMapper instance
	 * - to be managed by SC
	 */
	@Bean
	ModelMapper modelMapper() {
	    ModelMapper mapper = new ModelMapper();

	    mapper.getConfiguration()
	            .setMatchingStrategy(MatchingStrategies.STRICT)
	            .setPropertyCondition(Conditions.isNotNull());

	    return mapper;
	}
}
