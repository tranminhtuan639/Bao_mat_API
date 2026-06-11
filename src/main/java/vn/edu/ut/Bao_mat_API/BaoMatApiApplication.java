package vn.edu.ut.Bao_mat_API;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaoMatApiApplication {
// .\mvnw.cmd spring-boot:run
	public static void main(String[] args) {
		// Load .env file
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();
		
		// Set system properties from .env
		if (dotenv.get("DB_URL") != null) System.setProperty("DB_URL", dotenv.get("DB_URL"));
		if (dotenv.get("DB_USERNAME") != null) System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		if (dotenv.get("DB_PASSWORD") != null) System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		if (dotenv.get("JWT_SECRET") != null) System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		if (dotenv.get("JWT_EXPIRATION") != null) System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
		if (dotenv.get("GOOGLE_CLIENT_ID") != null) System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		if (dotenv.get("GOOGLE_CLIENT_SECRET") != null) System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		
		SpringApplication.run(BaoMatApiApplication.class, args);
	}
// PUT /api/users/{id}/role?role=ADMIN
}
