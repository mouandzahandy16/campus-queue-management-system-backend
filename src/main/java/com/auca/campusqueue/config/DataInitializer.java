package com.auca.campusqueue.config;

import com.auca.campusqueue.model.*;
import com.auca.campusqueue.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CampusServiceRepository campusServiceRepository;
    private final CounterRepository counterRepository;
    private final StaffMemberRepository staffMemberRepository;
    private final StudentRepository studentRepository;
    private final QueueTicketRepository queueTicketRepository;
    private final ServiceLogRepository serviceLogRepository;

    public DataInitializer(CampusServiceRepository campusServiceRepository,
                           CounterRepository counterRepository,
                           StaffMemberRepository staffMemberRepository,
                           StudentRepository studentRepository,
                           QueueTicketRepository queueTicketRepository,
                           ServiceLogRepository serviceLogRepository) {
        this.campusServiceRepository = campusServiceRepository;
        this.counterRepository = counterRepository;
        this.staffMemberRepository = staffMemberRepository;
        this.studentRepository = studentRepository;
        this.queueTicketRepository = queueTicketRepository;
        this.serviceLogRepository = serviceLogRepository;
    }

    @Override
    public void run(String... args) {
        if (counterRepository.count() > 0) {
            return;
        }

        CampusService regService = campusServiceRepository.save(new CampusService("Registration"));
        CampusService finService = campusServiceRepository.save(new CampusService("Finance"));
        CampusService libService = campusServiceRepository.save(new CampusService("Library Support"));
        campusServiceRepository.save(new CampusService("Academic Administration"));

        Counter c1 = counterRepository.save(new Counter(1, "Counter 1"));
        Counter c2 = counterRepository.save(new Counter(2, "Counter 2"));
        Counter c3 = counterRepository.save(new Counter(3, "Counter 3"));
        counterRepository.save(new Counter(4, "Counter 4"));

        StaffMember s1 = new StaffMember("Alice", "Mugisha", "alice.mugisha@auca.ac.rw");
        s1.setCounter(c1);
        s1.getCampusServices().add(regService);
        s1.getCampusServices().add(finService);
        s1 = staffMemberRepository.save(s1);

        StaffMember s2 = new StaffMember("Bob", "Kamanzi", "bob.kamanzi@auca.ac.rw");
        s2.setCounter(c2);
        s2.getCampusServices().add(libService);
        s2 = staffMemberRepository.save(s2);

        StaffMember s3 = new StaffMember("Claire", "Uwase", "claire.uwase@auca.ac.rw");
        s3.setCounter(c3);
        s3.getCampusServices().add(regService);
        s3 = staffMemberRepository.save(s3);

        Student st1 = studentRepository.save(new Student("Handy Christopher", "Mouandza", "27284", "handy@auca.ac.rw", "+250788111222"));
        Student st2 = studentRepository.save(new Student("Grace", "Mouandza", "27285", "grace@auca.ac.rw", "+250788222333"));
        Student st3 = studentRepository.save(new Student("David", "Habimana", "27286", "david@auca.ac.rw", "+250788333444"));
        Student st4 = studentRepository.save(new Student("Eric", "Ndayishimiye", "27287", "eric@auca.ac.rw", "+250788444555"));
        Student st5 = studentRepository.save(new Student("Fiona", "Ingabire", "27288", "fiona@auca.ac.rw", "+250788555666"));

        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 10, 20), TicketStatus.SKIPPED, "T-101", st1, s1));
        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 10, 20), TicketStatus.WAITING, "T-102", st2, s1));

        QueueTicket t3 = queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 10, 22), TicketStatus.SERVED, "T-103", st1, s2));
        QueueTicket t4 = queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 10, 25), TicketStatus.SERVED, "T-104", st3, s3));

        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 10, 28), TicketStatus.CALLED, "T-105", st1, s1));
        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 10, 30), TicketStatus.WAITING, "T-106", st4, s2));
        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 11, 2), TicketStatus.WAITING, "T-107", st5, s3));
        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 11, 5), TicketStatus.CALLED, "T-108", st2, s1));
        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 11, 8), TicketStatus.SKIPPED, "T-109", st3, s2));
        queueTicketRepository.save(new QueueTicket(LocalDate.of(2026, 11, 10), TicketStatus.WAITING, "T-110", st1, s3));

        serviceLogRepository.save(new ServiceLog("Registration cleared", "Student registered for Semester 1", 10, LocalDate.of(2026, 10, 22), t3));
        serviceLogRepository.save(new ServiceLog("Fee statement printed", "Student advised on balance", 7, LocalDate.of(2026, 10, 25), t4));
    }
}
