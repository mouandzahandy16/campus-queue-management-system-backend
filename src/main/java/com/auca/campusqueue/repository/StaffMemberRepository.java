package com.auca.campusqueue.repository;

import com.auca.campusqueue.model.Counter;
import com.auca.campusqueue.model.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, UUID> {

    @Query("SELECT DISTINCT s FROM StaffMember s JOIN s.campusServices c WHERE LOWER(c.name) = LOWER(:name)")
    List<StaffMember> findByCampusServiceNameIgnoreCase(@Param("name") String name);

    boolean existsByCounter(Counter counter);
}
