package dev.ewd.mediashelf_spring.mediashelf_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "dev.ewd.mediashelf_spring.mediashelf_spring.model")
public class MediashelfSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediashelfSpringApplication.class, args);
	}

}
