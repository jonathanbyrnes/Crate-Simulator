package core.craft.openingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OpeningServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpeningServiceApplication.class, args);
	}

}
