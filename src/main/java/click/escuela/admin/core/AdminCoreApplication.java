package click.escuela.admin.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AdminCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminCoreApplication.class, args);
	}
	

}
