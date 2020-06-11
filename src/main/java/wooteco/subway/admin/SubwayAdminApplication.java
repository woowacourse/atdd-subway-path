package wooteco.subway.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SubwayAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayAdminApplication.class, args);
	}

}
