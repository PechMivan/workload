package epam.microservice.workload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class WorkloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkloadApplication.class, args);
	}

}
