package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.service.QueueTicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/queue-tickets")
public class QueueTicketController {

    private final QueueTicketService queueTicketService;

    public QueueTicketController(QueueTicketService queueTicketService) {
        this.queueTicketService = queueTicketService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody QueueTicket queueTicket) {
        String message = queueTicketService.save(queueTicket);
        if (message.equals("Queue ticket date cannot be in the past")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        } else if (message.equals("Staff member already has a ticket on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<QueueTicket>> getAll() {
        return ResponseEntity.ok(queueTicketService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<QueueTicket> queueTicket = queueTicketService.getById(id);
        if (queueTicket.isPresent()) {
            return ResponseEntity.ok(queueTicket.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The queue ticket with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody QueueTicket queueTicket) {
        String message = queueTicketService.update(id, queueTicket);
        if (message.equals("The queue ticket with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = queueTicketService.delete(id);
        if (message.equals("The queue ticket with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("That queue ticket has service logs, delete them first")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<QueueTicket>> getByStatus(@RequestParam TicketStatus status) {
        return ResponseEntity.ok(queueTicketService.getByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<QueueTicket>> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(queueTicketService.getBetweenDates(startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam TicketStatus status) {
        String message = queueTicketService.updateStatus(id, status);
        if (message.equals("The queue ticket with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.startsWith("Cannot change status from")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStatsByStatus() {
        return ResponseEntity.ok(queueTicketService.getStatsByStatus());
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID staffMemberId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        String message = queueTicketService.cancelDay(staffMemberId, localDate);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<QueueTicket>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ticketDate,desc") String sort) {
        String[] sortParts = sort.split(",");
        String property = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));
        return ResponseEntity.ok(queueTicketService.getPage(pageable));
    }
}
