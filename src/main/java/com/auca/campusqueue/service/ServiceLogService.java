package com.auca.campusqueue.service;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.ServiceLog;
import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.repository.QueueTicketRepository;
import com.auca.campusqueue.repository.ServiceLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceLogService {

    private final ServiceLogRepository serviceLogRepository;
    private final QueueTicketRepository queueTicketRepository;

    public ServiceLogService(ServiceLogRepository serviceLogRepository,
                             QueueTicketRepository queueTicketRepository) {
        this.serviceLogRepository = serviceLogRepository;
        this.queueTicketRepository = queueTicketRepository;
    }

    public String save(UUID queueTicketId, ServiceLog serviceLog) {
        Optional<QueueTicket> ticketOpt = queueTicketRepository.findById(queueTicketId);
        if (ticketOpt.isEmpty()) {
            return "The queue ticket with that id does not exist";
        }
        QueueTicket ticket = ticketOpt.get();
        if (ticket.getStatus() != TicketStatus.SERVED) {
            return "Service logs are only for SERVED queue tickets";
        }

        serviceLog.setLoggedDate(LocalDate.now());
        serviceLog.setQueueTicket(ticket);
        serviceLogRepository.save(serviceLog);
        return "Service log saved successfully";
    }

    public List<ServiceLog> getByStudentRegNumber(String regNumber) {
        return serviceLogRepository.findServiceLogsByStudentRegNumber(regNumber);
    }

    public String delete(UUID id) {
        Optional<ServiceLog> existing = serviceLogRepository.findById(id);
        if (existing.isEmpty()) {
            return "The service log with that id does not exist";
        }
        serviceLogRepository.delete(existing.get());
        return "Service log deleted successfully";
    }

    public List<ServiceLog> getAll() {
        return serviceLogRepository.findAll();
    }
}
