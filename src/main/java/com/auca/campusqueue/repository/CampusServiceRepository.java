package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.CampusService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampusServiceRepository extends JpaRepository<CampusService, UUID> {

    @Query("SELECT c FROM CampusService c WHERE c.staffMembers IS EMPTY")
    List<CampusService> findUnusedServices();
}
