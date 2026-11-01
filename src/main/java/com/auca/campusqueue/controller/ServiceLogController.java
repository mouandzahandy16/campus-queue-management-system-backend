package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.ServiceLog;
import com.auca.campusqueue.service.ServiceLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-logs")
public class ServiceLogController {

    private final ServiceLogService serviceLogService;

    public ServiceLogController(ServiceLogService serviceLogService) {
        this.serviceLogService = serviceLogService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID queueTicketId, @RequestBody ServiceLog serviceLog) {
        String message = serviceLogService.save(queueTicketId, serviceLog);
        if (message.equals("The queue ticket with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("Service logs are only for SERVED queue tickets")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
    }

    @GetMapping("/student/{regNumber}")
    public ResponseEntity<List<ServiceLog>> getByStudent(@PathVariable String regNumber) {
        return ResponseEntity.ok(serviceLogService.getByStudentRegNumber(regNumber));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = serviceLogService.delete(id);
        if (message.equals("The service log with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceLog>> getAll() {
        return ResponseEntity.ok(serviceLogService.getAll());
    }
}
