package solutions.dmitrikonnov.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ETEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ETEurekaServerApplication.class, args);
    }
}
