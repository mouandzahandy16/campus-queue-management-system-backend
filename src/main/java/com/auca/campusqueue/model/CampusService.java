package com.auca.campusqueue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "campus_service")
public class CampusService {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "campusServices")
    @JsonIgnore
    private List<StaffMember> staffMembers = new ArrayList<>();

    public CampusService() {
    }

    public CampusService(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StaffMember> getStaffMembers() {
        return staffMembers;
    }

    public void setStaffMembers(List<StaffMember> staffMembers) {
        this.staffMembers = staffMembers;
    }
}
