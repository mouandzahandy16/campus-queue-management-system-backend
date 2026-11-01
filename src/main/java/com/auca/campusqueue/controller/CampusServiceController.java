package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.CampusService;
import com.auca.campusqueue.service.CampusServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/campus-services")
public class CampusServiceController {

    private final CampusServiceService campusServiceService;

    public CampusServiceController(CampusServiceService campusServiceService) {
        this.campusServiceService = campusServiceService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody CampusService campusService) {
        String message = campusServiceService.save(campusService);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CampusService>> getAll() {
        return ResponseEntity.ok(campusServiceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<CampusService> campusService = campusServiceService.getById(id);
        if (campusService.isPresent()) {
            return ResponseEntity.ok(campusService.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The campus service with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody CampusService campusService) {
        String message = campusServiceService.update(id, campusService);
        if (message.equals("The campus service with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = campusServiceService.delete(id);
        if (message.equals("The campus service with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/unused")
    public ResponseEntity<List<CampusService>> getUnused() {
        return ResponseEntity.ok(campusServiceService.getUnused());
    }
}
