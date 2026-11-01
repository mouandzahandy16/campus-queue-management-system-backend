package com.auca.campusqueue.service;

import com.auca.campusqueue.model.CampusService;
import com.auca.campusqueue.repository.CampusServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampusServiceService {

    private final CampusServiceRepository campusServiceRepository;

    public CampusServiceService(CampusServiceRepository campusServiceRepository) {
        this.campusServiceRepository = campusServiceRepository;
    }

    public String save(CampusService campusService) {
        campusServiceRepository.save(campusService);
        return "Campus service saved successfully";
    }

    public List<CampusService> getAll() {
        return campusServiceRepository.findAll();
    }

    public Optional<CampusService> getById(UUID id) {
        return campusServiceRepository.findById(id);
    }

    public String update(UUID id, CampusService updatedService) {
        Optional<CampusService> existing = campusServiceRepository.findById(id);
        if (existing.isEmpty()) {
            return "The campus service with that id does not exist";
        }
        CampusService service = existing.get();
        service.setName(updatedService.getName());
        campusServiceRepository.save(service);
        return "Campus service updated successfully";
    }

    public String delete(UUID id) {
        Optional<CampusService> existing = campusServiceRepository.findById(id);
        if (existing.isEmpty()) {
            return "The campus service with that id does not exist";
        }
        campusServiceRepository.delete(existing.get());
        return "Campus service deleted successfully";
    }

    public List<CampusService> getUnused() {
        return campusServiceRepository.findUnusedServices();
    }
}
