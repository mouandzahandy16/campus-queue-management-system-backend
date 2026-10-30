package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.service.QueueTicketService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/queue-tickets")
public class QueueTicketController {

    private final QueueTicketService queueTicketService;

    public QueueTicketController(QueueTicketService queueTicketService) {
        this.queueTicketService = queueTicketService;
    }

    @GetMapping
    public List<QueueTicket> getAllTickets() {
        return queueTicketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public QueueTicket getTicketById(@PathVariable UUID id) {
        return queueTicketService.getTicketById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QueueTicket createTicket(@RequestBody QueueTicket ticket) {
        return queueTicketService.createTicket(ticket);
    }

    @PatchMapping("/{id}/status")
    public QueueTicket updateStatus(@PathVariable UUID id, @RequestParam TicketStatus status) {
        return queueTicketService.updateTicketStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable UUID id) {
        queueTicketService.deleteTicket(id);
    }

    @GetMapping("/report")
    public Map<String, Long> getReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return queueTicketService.getStatusReportForDate(date);
    }

    @PostMapping("/bulk-cancel")
    public Map<String, Object> bulkCancel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int updatedCount = queueTicketService.bulkCancelUnservedTickets(date);
        return Map.of("date", date.toString(), "cancelledTicketsCount", updatedCount);
    }
}