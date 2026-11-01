package com.auca.campusqueue.service;

import com.auca.campusqueue.model.Counter;
import com.auca.campusqueue.repository.CounterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CounterService {

    private final CounterRepository counterRepository;

    public CounterService(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    public String save(Counter counter) {
        counterRepository.save(counter);
        return "Counter saved successfully";
    }

    public List<Counter> getAll() {
        return counterRepository.findAll();
    }

    public Optional<Counter> getById(UUID id) {
        return counterRepository.findById(id);
    }

    public String update(UUID id, Counter updatedCounter) {
        Optional<Counter> existing = counterRepository.findById(id);
        if (existing.isEmpty()) {
            return "The counter with that id does not exist";
        }
        Counter counter = existing.get();
        counter.setCounterNumber(updatedCounter.getCounterNumber());
        counter.setName(updatedCounter.getName());
        counterRepository.save(counter);
        return "Counter updated successfully";
    }

    public String delete(UUID id) {
        Optional<Counter> existing = counterRepository.findById(id);
        if (existing.isEmpty()) {
            return "The counter with that id does not exist";
        }
        counterRepository.delete(existing.get());
        return "Counter deleted successfully";
    }

    public List<Counter> getFreeCounters() {
        return counterRepository.findFreeCounters();
    }
}
