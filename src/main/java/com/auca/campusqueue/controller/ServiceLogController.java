package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.ServiceLog;
import com.auca.campusqueue.service.ServiceLogService;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<ServiceLog> getAllLogs() {
        return serviceLogService.getAllLogs();
    }

    @GetMapping("/ticket/{ticketId}")
    public List<ServiceLog> getLogsByTicket(@PathVariable UUID ticketId) {
        return serviceLogService.getLogsByTicketId(ticketId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceLog createLog(@RequestBody ServiceLog log) {
        return serviceLogService.createLog(log);
    }
}