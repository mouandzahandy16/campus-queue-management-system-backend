package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceLogRepository extends JpaRepository<ServiceLog, UUID> {

    @Query("SELECT sl FROM ServiceLog sl WHERE sl.queueTicket.student.regNumber = :regNumber ORDER BY sl.loggedDate DESC")
    List<ServiceLog> findServiceLogsByStudentRegNumber(@Param("regNumber") String regNumber);

    boolean existsByQueueTicket(QueueTicket queueTicket);
}
