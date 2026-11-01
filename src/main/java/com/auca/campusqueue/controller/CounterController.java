package com.auca.campusqueue.controller;

import com.auca.campusqueue.model.Counter;
import com.auca.campusqueue.service.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/counters")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Counter counter) {
        String message = counterService.save(counter);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Counter>> getAll() {
        return ResponseEntity.ok(counterService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Counter> counter = counterService.getById(id);
        if (counter.isPresent()) {
            return ResponseEntity.ok(counter.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The counter with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Counter counter) {
        String message = counterService.update(id, counter);
        if (message.equals("The counter with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = counterService.delete(id);
        if (message.equals("The counter with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/free")
    public ResponseEntity<List<Counter>> getFreeCounters() {
        return ResponseEntity.ok(counterService.getFreeCounters());
    }
}
