package com.student.details.student;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class StudentService {

	private final StudentRepository studentRepository;

	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Cacheable("students")
	public List<Student> findAll() {
		return studentRepository.findAll();
	}

	@Cacheable(value = "student", key = "#id")
	public Student findById(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
	}

	@Transactional
	@CacheEvict(cacheNames = { "students", "student" }, allEntries = true)
	public Student create(StudentRequest request) {
		return studentRepository.save(new Student(request.name(), request.email(), request.course()));
	}

	@Transactional
	@CacheEvict(cacheNames = { "students", "student" }, allEntries = true)
	public Student update(Long id, StudentRequest request) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
		student.setName(request.name());
		student.setEmail(request.email());
		student.setCourse(request.course());
		return studentRepository.save(student);
	}

	@Transactional
	@CacheEvict(cacheNames = { "students", "student" }, allEntries = true)
	public void delete(Long id) {
		if (!studentRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
		}
		studentRepository.deleteById(id);
	}
}
