package com.student.details;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	@Bean
	JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain applicationSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				.formLogin(Customizer.withDefaults())
				.build();
	}
}
