package com.auca.campusqueue.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_logs")
public class ServiceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "queue_ticket_id", nullable = false)
    private QueueTicket queueTicket;

    @Column(nullable = false)
    private LocalDateTime loggedAt;

    @Column(length = 1000)
    private String notes;

    public ServiceLog() {
    }

    public ServiceLog(QueueTicket queueTicket, LocalDateTime loggedAt, String notes) {
        this.queueTicket = queueTicket;
        this.loggedAt = loggedAt;
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public QueueTicket getQueueTicket() {
        return queueTicket;
    }

    public void setQueueTicket(QueueTicket queueTicket) {
        this.queueTicket = queueTicket;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}