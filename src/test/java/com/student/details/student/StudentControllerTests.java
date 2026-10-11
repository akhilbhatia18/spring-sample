package com.student.details.student;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CacheManager cacheManager;

	@Test
	void authenticatesSeededDatabaseUserUsingHashedPassword() throws Exception {
		String storedPassword = jdbcTemplate.queryForObject(
				"SELECT password FROM users WHERE username = ?",
				String.class,
				"student");

		assertNotEquals("Student@123", storedPassword);
		assertTrue(passwordEncoder.matches("Student@123", storedPassword));

		mockMvc.perform(get("/api/students").with(httpBasic("student", "Student@123")))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/students").with(httpBasic("student", "wrong-password")))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void exposesHealthChecksWithoutAuthentication() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
		mockMvc.perform(get("/actuator/health/liveness"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
		mockMvc.perform(get("/actuator/health/readiness"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void supportsStudentCrud() throws Exception {
		studentRepository.deleteAll();
		cacheManager.getCache("students").clear();
		cacheManager.getCache("student").clear();
		mockMvc.perform(get("/api/students").with(user("student-api-test")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());

		String studentJson = """
				{"name":"Alex Student","email":"alex@example.com","course":"Computer Science"}
				""";

		String location = mockMvc.perform(post("/api/students")
						.with(user("student-api-test"))
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJson))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Alex Student"))
				.andReturn()
				.getResponse()
				.getHeader("Location");
		Long id = Long.valueOf(URI.create(location).getPath().substring("/api/students/".length()));

		mockMvc.perform(get("/api/students").with(user("student-api-test")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Alex Student"));
		mockMvc.perform(get("/api/students/{id}", id).with(user("student-api-test")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("alex@example.com"));

		mockMvc.perform(put("/api/students/{id}", id)
						.with(user("student-api-test"))
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Alex Updated","email":"alex.updated@example.com","course":"Mathematics"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Alex Updated"));
		mockMvc.perform(get("/api/students/{id}", id).with(user("student-api-test")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Alex Updated"));

		mockMvc.perform(delete("/api/students/{id}", id)
						.with(user("student-api-test"))
						.with(csrf()))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/students/{id}", id).with(user("student-api-test")))
				.andExpect(status().isNotFound());
	}
}
