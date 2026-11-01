package com.auca.campusqueue.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "service_log")
public class ServiceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String actionTaken;

    @Column(nullable = false)
    private String remarks;

    @Column(nullable = false)
    private int minutesSpent;

    @Column(nullable = false)
    private LocalDate loggedDate;

    @ManyToOne
    @JoinColumn(name = "queue_ticket_id", nullable = false)
    private QueueTicket queueTicket;

    public ServiceLog() {
    }

    public ServiceLog(String actionTaken, String remarks, int minutesSpent, LocalDate loggedDate, QueueTicket queueTicket) {
        this.actionTaken = actionTaken;
        this.remarks = remarks;
        this.minutesSpent = minutesSpent;
        this.loggedDate = loggedDate;
        this.queueTicket = queueTicket;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(int minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public LocalDate getLoggedDate() {
        return loggedDate;
    }

    public void setLoggedDate(LocalDate loggedDate) {
        this.loggedDate = loggedDate;
    }

    public QueueTicket getQueueTicket() {
        return queueTicket;
    }

    public void setQueueTicket(QueueTicket queueTicket) {
        this.queueTicket = queueTicket;
    }
}
