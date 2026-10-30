package com.auca.campusqueue.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "staff_member")
public class StaffMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "counter_id", nullable = true)
    private Counter counter;

    @ManyToMany
    @JoinTable(
        name = "staff_member_campus_service",
        joinColumns = @JoinColumn(name = "staff_member_id"),
        inverseJoinColumns = @JoinColumn(name = "campus_service_id")
    )
    private List<CampusService> campusServices = new ArrayList<>();

    public StaffMember() {
    }

    public StaffMember(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public List<CampusService> getCampusServices() {
        return campusServices;
    }

    public void setCampusServices(List<CampusService> campusServices) {
        this.campusServices = campusServices;
    }
}
