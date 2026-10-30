package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceLogRepository extends JpaRepository<ServiceLog, UUID> {

    List<ServiceLog> findByQueueTicketId(UUID ticketId);

    boolean existsByQueueTicket(QueueTicket queueTicket);
}