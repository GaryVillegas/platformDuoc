package com.backend.platformDuoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PlatformDuocApplication {

	public static void main(String[] args) {
		//Load env configuration
		Dotenv dotenv = Dotenv.configure().filename(".env")
		.ignoreIfMissing().load();

		System.setProperty("MYSQL_DATABASE", dotenv.get("MYSQL_DATABASE"));
		System.setProperty("MYSQL_USERNAME", dotenv.get("MYSQL_USERNAME"));
		System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));

		SpringApplication.run(PlatformDuocApplication.class, args);
	}

}
