package com.aakil.finance_dashboard_backend;

import com.aakil.finance_dashboard_backend.user.entity.Role;
import com.aakil.finance_dashboard_backend.user.entity.User;
import com.aakil.finance_dashboard_backend.user.entity.UserStatus;
import com.aakil.finance_dashboard_backend.user.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FinanceDashboardBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceDashboardBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			seedUser(userRepository, passwordEncoder, "superadmin@example.com", "Super Admin", "superadmin123", Role.ADMIN);
			seedUser(userRepository, passwordEncoder, "analyst@example.com", "Jane Doe", "analyst123", Role.ANALYST);
			seedUser(userRepository, passwordEncoder, "viewer@example.com", "John Doe", "viewer123", Role.VIEWER);
		};
	}

	private void seedUser(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder,
		String email,
		String name,
		String rawPassword,
		Role role
	) {
		if (userRepository.existsByEmail(email)) {
			return;
		}

		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setRole(role);
		user.setStatus(UserStatus.ACTIVE);

		userRepository.save(user);
	}

}
