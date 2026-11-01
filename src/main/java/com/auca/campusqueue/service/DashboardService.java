package com.auca.campusqueue.service;

import com.auca.campusqueue.model.QueueTicket;
import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final CampusServiceRepository campusServiceRepository;
    private final CounterRepository counterRepository;
    private final StaffMemberRepository staffMemberRepository;
    private final QueueTicketRepository queueTicketRepository;
    private final ServiceLogRepository serviceLogRepository;

    public DashboardService(StudentRepository studentRepository,
                            CampusServiceRepository campusServiceRepository,
                            CounterRepository counterRepository,
                            StaffMemberRepository staffMemberRepository,
                            QueueTicketRepository queueTicketRepository,
                            ServiceLogRepository serviceLogRepository) {
        this.studentRepository = studentRepository;
        this.campusServiceRepository = campusServiceRepository;
        this.counterRepository = counterRepository;
        this.staffMemberRepository = staffMemberRepository;
        this.queueTicketRepository = queueTicketRepository;
        this.serviceLogRepository = serviceLogRepository;
    }

    public Map<String, Object> getSummaryMetrics() {
        LocalDate today = LocalDate.now();

        long totalStudents = studentRepository.count();
        long totalServices = campusServiceRepository.count();
        long totalCounters = counterRepository.count();
        long totalStaff = staffMemberRepository.count();
        long totalTickets = queueTicketRepository.count();
        long totalLogs = serviceLogRepository.count();

        long todayWaiting = queueTicketRepository.findByTicketDateAndStatus(today, TicketStatus.WAITING).size();
        long todayInProgress = queueTicketRepository.findByTicketDateAndStatus(today, TicketStatus.IN_PROGRESS).size();
        long todayServed = queueTicketRepository.findByTicketDateAndStatus(today, TicketStatus.SERVED).size();
        long todaySkipped = queueTicketRepository.findByTicketDateAndStatus(today, TicketStatus.SKIPPED).size();

        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("date", today.toString());
        metrics.put("totalStudents", totalStudents);
        metrics.put("totalCampusServices", totalServices);
        metrics.put("totalCounters", totalCounters);
        metrics.put("totalStaffMembers", totalStaff);
        metrics.put("totalQueueTickets", totalTickets);
        metrics.put("totalServiceLogs", totalLogs);

        Map<String, Long> todayStats = new LinkedHashMap<>();
        todayStats.put("WAITING", todayWaiting);
        todayStats.put("IN_PROGRESS", todayInProgress);
        todayStats.put("SERVED", todayServed);
        todayStats.put("SKIPPED", todaySkipped);
        metrics.put("todayTicketsByStatus", todayStats);

        return metrics;
    }

    public Page<QueueTicket> getTicketsPaged(Pageable pageable) {
        return queueTicketRepository.findAll(pageable);
    }
}