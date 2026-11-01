package com.auca.campusqueue.service;

import com.auca.campusqueue.model.TicketStatus;
import com.auca.campusqueue.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final StaffMemberRepository staffMemberRepository;
    private final CounterRepository counterRepository;
    private final QueueTicketRepository queueTicketRepository;
    private final ServiceLogRepository serviceLogRepository;

    public DashboardService(StudentRepository studentRepository,
                            StaffMemberRepository staffMemberRepository,
                            CounterRepository counterRepository,
                            QueueTicketRepository queueTicketRepository,
                            ServiceLogRepository serviceLogRepository) {
        this.studentRepository = studentRepository;
        this.staffMemberRepository = staffMemberRepository;
        this.counterRepository = counterRepository;
        this.queueTicketRepository = queueTicketRepository;
        this.serviceLogRepository = serviceLogRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("students", studentRepository.count());
        summary.put("staffMembers", staffMemberRepository.count());
        summary.put("freeCounters", (long) counterRepository.findFreeCounters().size());
        summary.put("ticketsToday", queueTicketRepository.countByTicketDate(LocalDate.now()));
        summary.put("waiting", queueTicketRepository.countByStatus(TicketStatus.WAITING));
        summary.put("serviceLogs", serviceLogRepository.count());
        return summary;
    }
}
