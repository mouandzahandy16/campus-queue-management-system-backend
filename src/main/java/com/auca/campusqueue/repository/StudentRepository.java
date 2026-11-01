package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    List<Student> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT q.student FROM QueueTicket q WHERE q.staffMember.id = :staffMemberId")
    List<Student> findStudentsByStaffMemberId(@Param("staffMemberId") UUID staffMemberId);

    Optional<Student> findByRegNumber(String regNumber);
}
