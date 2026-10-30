package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.StaffMember;
import com.auca.campusqueue.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface QueueTicketRepository extends JpaRepository<QueueTicket, UUID> {

    List<QueueTicket> findByCampusServiceIdAndTicketDateOrderByTicketNumberAsc(UUID campusServiceId, LocalDate ticketDate);

    List<QueueTicket> findByStudentIdAndTicketDateBetween(UUID studentId, LocalDate startDate, LocalDate endDate);

    List<QueueTicket> findByTicketDateAndStatus(LocalDate ticketDate, TicketStatus status);

    boolean existsByStaffMemberAndTicketDateAndStatusNot(StaffMember staffMember, LocalDate ticketDate, TicketStatus status);

    @Query("SELECT q.status, COUNT(q) FROM QueueTicket q WHERE q.ticketDate = :date GROUP BY q.status")
    List<Object[]> countTicketsByStatusForDate(@Param("date") LocalDate date);

    @Modifying
    @Query("UPDATE QueueTicket q SET q.status = 'SKIPPED' WHERE q.ticketDate = :date AND q.status IN ('WAITING', 'IN_PROGRESS')")
    int bulkCancelUnservedTicketsForDate(@Param("date") LocalDate date);
}