package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.StaffMember;
import com.auca.campusqueue.service.StaffMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff-members")
public class StaffMemberController {

    private final StaffMemberService staffMemberService;

    public StaffMemberController(StaffMemberService staffMemberService) {
        this.staffMemberService = staffMemberService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody StaffMember staffMember) {
        String message = staffMemberService.save(staffMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StaffMember>> getAll() {
        return ResponseEntity.ok(staffMemberService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<StaffMember> staffMember = staffMemberService.getById(id);
        if (staffMember.isPresent()) {
            return ResponseEntity.ok(staffMember.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The staff member with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody StaffMember staffMember) {
        String message = staffMemberService.update(id, staffMember);
        if (message.equals("The staff member with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = staffMemberService.delete(id);
        if (message.equals("The staff member with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/by-campus-service")
    public ResponseEntity<List<StaffMember>> getByCampusService(@RequestParam String name) {
        return ResponseEntity.ok(staffMemberService.getByCampusService(name));
    }

    @PutMapping("/{staffMemberId}/campus-services/{campusServiceId}")
    public ResponseEntity<String> addCampusService(@PathVariable UUID staffMemberId, @PathVariable UUID campusServiceId) {
        String message = staffMemberService.addCampusService(staffMemberId, campusServiceId);
        if (message.equals("The staff member with that id does not exist") || message.equals("That campus service does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("The staff member already has that campus service")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @PatchMapping("/{staffMemberId}/counter")
    public ResponseEntity<String> changeCounter(@PathVariable UUID staffMemberId, @RequestParam int counterNumber) {
        String message = staffMemberService.changeCounter(staffMemberId, counterNumber);
        if (message.equals("The staff member with that id does not exist") || message.equals("The counter with that counterNumber does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("That counter is taken by another staff member")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
