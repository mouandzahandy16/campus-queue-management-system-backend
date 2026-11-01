package com.auca.campusqueue.service;

import com.auca.campusqueue.model.CampusService;
import com.auca.campusqueue.model.Counter;
import com.auca.campusqueue.model.StaffMember;
import com.auca.campusqueue.repository.CampusServiceRepository;
import com.auca.campusqueue.repository.CounterRepository;
import com.auca.campusqueue.repository.StaffMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public String save(StaffMember staffMember) {
        staffMemberRepository.save(staffMember);
        return "Staff member saved successfully";
    }

    public List<StaffMember> getAll() {
        return staffMemberRepository.findAll();
    }

    public Optional<StaffMember> getById(UUID id) {
        return staffMemberRepository.findById(id);
    }

    public String update(UUID id, StaffMember updatedStaff) {
        Optional<StaffMember> existing = staffMemberRepository.findById(id);
        if (existing.isEmpty()) {
            return "The staff member with that id does not exist";
        }
        StaffMember staff = existing.get();
        staff.setFirstName(updatedStaff.getFirstName());
        staff.setLastName(updatedStaff.getLastName());
        staff.setEmail(updatedStaff.getEmail());
        staff.setCounter(updatedStaff.getCounter());
        staff.setCampusServices(updatedStaff.getCampusServices());
        staffMemberRepository.save(staff);
        return "Staff member updated successfully";
    }

    public String delete(UUID id) {
        Optional<StaffMember> existing = staffMemberRepository.findById(id);
        if (existing.isEmpty()) {
            return "The staff member with that id does not exist";
        }
        staffMemberRepository.delete(existing.get());
        return "Staff member deleted successfully";
    }

    public List<StaffMember> getByCampusService(String name) {
        return staffMemberRepository.findByCampusServiceNameIgnoreCase(name);
    }

    public String addCampusService(UUID staffMemberId, UUID campusServiceId) {
        Optional<StaffMember> staffOpt = staffMemberRepository.findById(staffMemberId);
        if (staffOpt.isEmpty()) {
            return "The staff member with that id does not exist";
        }
        Optional<CampusService> serviceOpt = campusServiceRepository.findById(campusServiceId);
        if (serviceOpt.isEmpty()) {
            return "That campus service does not exist";
        }
        StaffMember staff = staffOpt.get();
        CampusService service = serviceOpt.get();

        if (staff.getCampusServices().contains(service)) {
            return "The staff member already has that campus service";
        }

        staff.getCampusServices().add(service);
        staffMemberRepository.save(staff);
        return "Campus service added to the staff member";
    }

    public String changeCounter(UUID staffMemberId, int counterNumber) {
        Optional<StaffMember> staffOpt = staffMemberRepository.findById(staffMemberId);
        if (staffOpt.isEmpty()) {
            return "The staff member with that id does not exist";
        }
        Optional<Counter> counterOpt = counterRepository.findByCounterNumber(counterNumber);
        if (counterOpt.isEmpty()) {
            return "The counter with that counterNumber does not exist";
        }
        Counter counter = counterOpt.get();
        StaffMember staff = staffOpt.get();

        if (staffMemberRepository.existsByCounter(counter)) {
            if (staff.getCounter() == null || !staff.getCounter().getId().equals(counter.getId())) {
                return "That counter is taken by another staff member";
            }
        }

        staff.setCounter(counter);
        staffMemberRepository.save(staff);
        return "Counter changed successfully";
    }
}
