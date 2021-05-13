package wooteco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;
import wooteco.auth.domain.Member;

@SpringBootApplication
public class SubwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayApplication.class, args);
	}

	@Bean
	@SessionScope
	public Member member() {
		return Member.empty();
	}
}
