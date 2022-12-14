package solutions.dmitrikonnov.einstufungstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient

public class EinstufungsTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(EinstufungsTestApplication.class, args);
	}

}
