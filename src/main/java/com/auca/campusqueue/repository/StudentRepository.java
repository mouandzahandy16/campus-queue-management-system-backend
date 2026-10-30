package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentIdNumber(String studentIdNumber);

    List<Student> findByFullNameContainingIgnoreCase(String keyword);
}