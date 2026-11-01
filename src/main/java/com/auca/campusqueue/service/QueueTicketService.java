package com.auca.campusqueue.service;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.repository.QueueTicketRepository;
import com.auca.campusqueue.repository.ServiceLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QueueTicketService {

    private final QueueTicketRepository queueTicketRepository;
    private final ServiceLogRepository serviceLogRepository;

    public QueueTicketService(QueueTicketRepository queueTicketRepository,
                              ServiceLogRepository serviceLogRepository) {
        this.queueTicketRepository = queueTicketRepository;
        this.serviceLogRepository = serviceLogRepository;
    }

    public String save(QueueTicket queueTicket) {
        if (queueTicket.getTicketDate() == null || queueTicket.getTicketDate().isBefore(LocalDate.now())) {
            return "Queue ticket date cannot be in the past";
        }

        if (queueTicket.getStaffMember() != null && queueTicket.getStaffMember().getId() != null) {
            boolean hasActiveTicket = queueTicketRepository.existsByStaffMemberAndTicketDateAndStatusNot(
                    queueTicket.getStaffMember(),
                    queueTicket.getTicketDate(),
                    TicketStatus.SKIPPED
            );
            if (hasActiveTicket) {
                return "Staff member already has a ticket on that date";
            }
        }

        queueTicket.setStatus(TicketStatus.WAITING);
        queueTicketRepository.save(queueTicket);
        return "Queue ticket saved successfully";
    }

    public List<QueueTicket> getAll() {
        return queueTicketRepository.findAll();
    }

    public Optional<QueueTicket> getById(UUID id) {
        return queueTicketRepository.findById(id);
    }

    public String update(UUID id, QueueTicket updatedTicket) {
        Optional<QueueTicket> existing = queueTicketRepository.findById(id);
        if (existing.isEmpty()) {
            return "The queue ticket with that id does not exist";
        }
        QueueTicket ticket = existing.get();
        ticket.setTicketDate(updatedTicket.getTicketDate());
        ticket.setTicketNumber(updatedTicket.getTicketNumber());
        ticket.setStudent(updatedTicket.getStudent());
        ticket.setStaffMember(updatedTicket.getStaffMember());
        queueTicketRepository.save(ticket);
        return "Queue ticket updated successfully";
    }

    public String delete(UUID id) {
        Optional<QueueTicket> existing = queueTicketRepository.findById(id);
        if (existing.isEmpty()) {
            return "The queue ticket with that id does not exist";
        }
        QueueTicket ticket = existing.get();
        if (serviceLogRepository.existsByQueueTicket(ticket)) {
            return "That queue ticket has service logs, delete them first";
        }
        queueTicketRepository.delete(ticket);
        return "Queue ticket deleted successfully";
    }

    public List<QueueTicket> getByStatus(TicketStatus status) {
        return queueTicketRepository.findByStatusOrderByTicketDateAsc(status);
    }

    public List<QueueTicket> getBetweenDates(LocalDate start, LocalDate end) {
        return queueTicketRepository.findByTicketDateBetweenOrderByTicketDateAsc(start, end);
    }

    public String updateStatus(UUID id, TicketStatus newStatus) {
        Optional<QueueTicket> existing = queueTicketRepository.findById(id);
        if (existing.isEmpty()) {
            return "The queue ticket with that id does not exist";
        }
        QueueTicket ticket = existing.get();
        TicketStatus current = ticket.getStatus();

        boolean allowed = false;
        if (current == TicketStatus.WAITING && (newStatus == TicketStatus.CALLED || newStatus == TicketStatus.SKIPPED)) {
            allowed = true;
        } else if (current == TicketStatus.CALLED && (newStatus == TicketStatus.SERVED || newStatus == TicketStatus.SKIPPED)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        ticket.setStatus(newStatus);
        queueTicketRepository.save(ticket);
        return "Queue ticket status updated successfully";
    }

    public List<Object[]> getStatsByStatus() {
        return queueTicketRepository.countTicketsByStatus();
    }

    @Transactional
    public String cancelDay(UUID staffMemberId, LocalDate date) {
        int count = queueTicketRepository.cancelDayTickets(staffMemberId, date);
        return count + " queue tickets cancelled";
    }

    public Page<QueueTicket> getPage(Pageable pageable) {
        return queueTicketRepository.findAll(pageable);
    }
}
