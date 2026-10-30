package com.auca.campusqueue.service;

import com.auca.campusqueue.model.CampusService;
import com.auca.campusqueue.model.Counter;
import com.auca.campusqueue.model.StaffMember;
import com.auca.campusqueue.repository.CampusServiceRepository;
import com.auca.campusqueue.repository.CounterRepository;
import com.auca.campusqueue.repository.StaffMemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class StaffMemberService {

    private final StaffMemberRepository staffMemberRepository;
    private final CampusServiceRepository campusServiceRepository;
    private final CounterRepository counterRepository;

    public StaffMemberService(StaffMemberRepository staffMemberRepository,
                              CampusServiceRepository campusServiceRepository,
                              CounterRepository counterRepository) {
        this.staffMemberRepository = staffMemberRepository;
        this.campusServiceRepository = campusServiceRepository;
        this.counterRepository = counterRepository;
    }

    public List<StaffMember> getAllStaffMembers() {
        return staffMemberRepository.findAll();
    }

    public StaffMember getStaffMemberById(UUID id) {
        return staffMemberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff member not found"));
    }

    public StaffMember createStaffMember(StaffMember staffMember) {
        return staffMemberRepository.save(staffMember);
    }

    public StaffMember assignService(UUID staffId, UUID serviceId) {
        StaffMember staff = getStaffMemberById(staffId);
        CampusService service = campusServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));

        if (!staff.getCampusServices().contains(service)) {
            staff.getCampusServices().add(service);
        }
        return staffMemberRepository.save(staff);
    }

    public StaffMember assignCounter(UUID staffId, UUID counterId) {
        StaffMember staff = getStaffMemberById(staffId);
        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Counter not found"));

        List<StaffMember> allStaff = staffMemberRepository.findAll();
        for (StaffMember other : allStaff) {
            if (!other.getId().equals(staffId) && other.getCounter() != null && other.getCounter().getId().equals(counterId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Counter is already assigned to another staff member");
            }
        }

        staff.setCounter(counter);
        return staffMemberRepository.save(staff);
    }

    public void deleteStaffMember(UUID id) {
        StaffMember staff = getStaffMemberById(id);
        staffMemberRepository.delete(staff);
    }
}