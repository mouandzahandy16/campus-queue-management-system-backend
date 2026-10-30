package com.auca.campusqueue.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "counter")
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private int counterNumber;

    @Column(nullable = false)
    private String name;

    public Counter() {
    }

    public Counter(int counterNumber, String name) {
        this.counterNumber = counterNumber;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(int counterNumber) {
        this.counterNumber = counterNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
