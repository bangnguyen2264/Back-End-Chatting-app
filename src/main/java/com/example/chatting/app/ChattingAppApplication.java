package com.example.chatting.app;

import com.example.chatting.app.model.User;
import com.example.chatting.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
public class ChattingAppApplication {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(ChattingAppApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	@Bean
	public CommandLineRunner initData() {
		return args -> {
			IntStream.rangeClosed(1, 20).forEach(i -> {
				String firstName = "User" +(21-i);
				String lastName = "Lastname" + i;
				String username = "user" + i;
				String email = "user" + i + "@gmail.com";
				String password = passwordEncoder.encode("password123");
				LocalDate dob = LocalDate.of(1990, 1, 1).plusDays(i);

				User user = User.builder()
						.firstname(firstName)
						.lastname(lastName)
						.dob(dob)
						.email(email)
						.username(username)
						.password(passwordEncoder.encode("password123"))
						.build();
				userRepository.save(user);

			});
		};
	}

}
