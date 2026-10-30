package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.StaffMember;
import com.auca.campusqueue.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, UUID> {

    @Query("SELECT s FROM StaffMember s JOIN s.campusServices cs WHERE LOWER(cs.name) = LOWER(:serviceName)")
    List<StaffMember> findStaffByServiceNameIgnoreCase(@Param("serviceName") String serviceName);

    @Query("SELECT DISTINCT q.student FROM QueueTicket q WHERE q.staffMember.id = :staffId")
    List<Student> findDistinctStudentsServedByStaff(@Param("staffId") UUID staffId);
}