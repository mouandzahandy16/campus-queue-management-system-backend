package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.Student;
import com.auca.campusqueue.service.StaffMemberService;
import com.auca.campusqueue.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final StaffMemberService staffMemberService;

    public StudentController(StudentService studentService, StaffMemberService staffMemberService) {
        this.studentService = studentService;
        this.staffMemberService = staffMemberService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Student student) {
        String message = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Student> student = studentService.getById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The student with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Student student) {
        String message = studentService.update(id, student);
        if (message.equals("The student with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = studentService.delete(id);
        if (message.equals("The student with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Student>> getByLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(studentService.getByLastName(lastName));
    }

    @GetMapping("/of-staff-member/{staffMemberId}")
    public ResponseEntity<?> getStudentsOfStaffMember(@PathVariable UUID staffMemberId) {
        if (staffMemberService.getById(staffMemberId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The staff member with that id does not exist");
        }
        return ResponseEntity.ok(studentService.getStudentsOfStaffMember(staffMemberId));
    }
}
