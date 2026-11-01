package com.auca.campusqueue.service;

import com.auca.campusqueue.model.Student;
import com.auca.campusqueue.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String save(Student student) {
        studentRepository.save(student);
        return "Student saved successfully";
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> getById(UUID id) {
        return studentRepository.findById(id);
    }

    public String update(UUID id, Student updatedStudent) {
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isEmpty()) {
            return "The student with that id does not exist";
        }
        Student student = existing.get();
        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setRegNumber(updatedStudent.getRegNumber());
        student.setEmail(updatedStudent.getEmail());
        student.setPhone(updatedStudent.getPhone());
        studentRepository.save(student);
        return "Student updated successfully";
    }

    public String delete(UUID id) {
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isEmpty()) {
            return "The student with that id does not exist";
        }
        studentRepository.delete(existing.get());
        return "Student deleted successfully";
    }

    public List<Student> getByLastName(String lastName) {
        return studentRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Student> getStudentsOfStaffMember(UUID staffMemberId) {
        return studentRepository.findStudentsByStaffMemberId(staffMemberId);
    }
}
