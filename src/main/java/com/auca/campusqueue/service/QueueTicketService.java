package com.auca.campusqueue.service;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.repository.QueueTicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class QueueTicketService {

    private final QueueTicketRepository queueTicketRepository;

    public QueueTicketService(QueueTicketRepository queueTicketRepository) {
        this.queueTicketRepository = queueTicketRepository;
    }

    public List<QueueTicket> getAllTickets() {
        return queueTicketRepository.findAll();
    }

    public QueueTicket getTicketById(UUID id) {
        return queueTicketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    public QueueTicket createTicket(QueueTicket ticket) {
        if (ticket.getTicketDate() != null && ticket.getTicketDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket date cannot be in the past");
        }

        if (ticket.getStaffMember() != null && ticket.getTicketDate() != null) {
            boolean hasActiveTicket = queueTicketRepository.existsByStaffMemberAndTicketDateAndStatusNot(
                    ticket.getStaffMember(),
                    ticket.getTicketDate(),
                    TicketStatus.SERVED
            );
            if (hasActiveTicket) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Staff member already has an active ticket for this date");
            }
        }

        if (ticket.getStatus() == null) {
            ticket.setStatus(TicketStatus.WAITING);
        }

        return queueTicketRepository.save(ticket);
    }

    public QueueTicket updateTicketStatus(UUID id, TicketStatus newStatus) {
        QueueTicket ticket = getTicketById(id);
        TicketStatus currentStatus = ticket.getStatus();

        if (currentStatus == TicketStatus.SERVED || currentStatus == TicketStatus.SKIPPED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change status of a finalized ticket");
        }

        if (currentStatus == TicketStatus.WAITING && newStatus != TicketStatus.IN_PROGRESS && newStatus != TicketStatus.SKIPPED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status transition from WAITING");
        }

        if (currentStatus == TicketStatus.IN_PROGRESS && newStatus != TicketStatus.SERVED && newStatus != TicketStatus.SKIPPED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status transition from IN_PROGRESS");
        }

        ticket.setStatus(newStatus);
        return queueTicketRepository.save(ticket);
    }

    public void deleteTicket(UUID id) {
        QueueTicket ticket = getTicketById(id);
        queueTicketRepository.delete(ticket);
    }
}