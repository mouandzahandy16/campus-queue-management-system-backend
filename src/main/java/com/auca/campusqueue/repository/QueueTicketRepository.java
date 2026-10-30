package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.StaffMember;
import com.auca.campusqueue.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QueueTicketRepository extends JpaRepository<QueueTicket, Long> {

    List<QueueTicket> findByCampusServiceIdAndTicketDateOrderByTicketNumberAsc(Long campusServiceId, LocalDate ticketDate);

    List<QueueTicket> findByStudentIdAndTicketDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    List<QueueTicket> findByTicketDateAndStatus(LocalDate ticketDate, TicketStatus status);
    boolean existsByStaffMemberAndTicketDateAndStatusNot(StaffMember staffMember, LocalDate ticketDate, TicketStatus status);
}