package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CounterRepository extends JpaRepository<Counter, UUID> {

    @Query("SELECT c FROM Counter c WHERE NOT EXISTS (SELECT s FROM StaffMember s WHERE s.counter = c) ORDER BY c.counterNumber ASC")
    List<Counter> findFreeCounters();

    Optional<Counter> findByCounterNumber(int counterNumber);
}
