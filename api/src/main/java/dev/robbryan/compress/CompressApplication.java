package dev.robbryan.compress;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompressApplication {
	public static void main(String[] args) {
		SpringApplication.run(CompressApplication.class, args);
	}

	public static byte[] readFileAsBytes(String filePath) throws Exception {
		return Files.readAllBytes(Paths.get(filePath));
	}
}
