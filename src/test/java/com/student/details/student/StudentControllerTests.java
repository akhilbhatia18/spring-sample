package com.student.details.student;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StudentRepository studentRepository;

	@Test
	void supportsStudentCrud() throws Exception {
		studentRepository.deleteAll();
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
