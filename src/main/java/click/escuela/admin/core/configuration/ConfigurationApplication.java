package click.escuela.admin.core.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties
public class ConfigurationApplication {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:4200"," http://clickescuela-web.s3-website.us-east-2.amazonaws.com","http://click-escuela-develop.herokuapp.com","https://click-escuela-master.herokuapp.com/")
					.allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
					.maxAge(3600);

			}

		};
	}
}

