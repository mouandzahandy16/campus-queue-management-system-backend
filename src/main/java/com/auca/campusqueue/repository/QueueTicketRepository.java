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

    List<QueueTicket> findByStatusOrderByTicketDateAsc(TicketStatus status);

    List<QueueTicket> findByTicketDateBetweenOrderByTicketDateAsc(LocalDate start, LocalDate end);

    boolean existsByStaffMemberAndTicketDateAndStatusNot(StaffMember staffMember, LocalDate ticketDate, TicketStatus status);

    @Query("SELECT q.status, COUNT(q) FROM QueueTicket q GROUP BY q.status")
    List<Object[]> countTicketsByStatus();

    @Modifying
    @Query("UPDATE QueueTicket q SET q.status = com.auca.campusqueue.model.TicketStatus.SKIPPED WHERE q.staffMember.id = :staffMemberId AND q.ticketDate = :date AND q.status != com.auca.campusqueue.model.TicketStatus.SERVED")
    int cancelDayTickets(@Param("staffMemberId") UUID staffMemberId, @Param("date") LocalDate date);

    long countByTicketDate(LocalDate ticketDate);

    long countByStatus(TicketStatus status);
}
