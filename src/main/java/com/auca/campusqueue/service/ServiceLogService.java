package com.auca.campusqueue.service;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.ServiceLog;
import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.repository.QueueTicketRepository;
import com.auca.campusqueue.repository.ServiceLogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceLogService {

    private final ServiceLogRepository serviceLogRepository;
    private final QueueTicketRepository queueTicketRepository;

    public ServiceLogService(ServiceLogRepository serviceLogRepository, QueueTicketRepository queueTicketRepository) {
        this.serviceLogRepository = serviceLogRepository;
        this.queueTicketRepository = queueTicketRepository;
    }

    public List<ServiceLog> getAllLogs() {
        return serviceLogRepository.findAll();
    }

    public List<ServiceLog> getLogsByTicketId(UUID ticketId) {
        return serviceLogRepository.findByQueueTicketId(ticketId);
    }

    public ServiceLog createLog(ServiceLog log) {
        if (log.getQueueTicket() == null || log.getQueueTicket().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Queue ticket reference is required");
        }

        QueueTicket ticket = queueTicketRepository.findById(log.getQueueTicket().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue ticket not found"));

        if (ticket.getStatus() != TicketStatus.SERVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service logs can only be created for SERVED tickets");
        }

        log.setQueueTicket(ticket);
        if (log.getLoggedAt() == null) {
            log.setLoggedAt(LocalDateTime.now());
        }

        return serviceLogRepository.save(log);
    }
}