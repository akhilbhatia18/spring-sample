package com.student.details.student;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	public List<Student> findAll() {
		return studentService.findAll();
	}

	@GetMapping("/{id}")
	public Student findById(@PathVariable Long id) {
		return studentService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Student> create(@RequestBody StudentRequest request) {
		Student student = studentService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(student.getId())
				.toUri();
		return ResponseEntity.created(location).body(student);
	}

	@PutMapping("/{id}")
	public Student update(@PathVariable Long id, @RequestBody StudentRequest request) {
		return studentService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		studentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
