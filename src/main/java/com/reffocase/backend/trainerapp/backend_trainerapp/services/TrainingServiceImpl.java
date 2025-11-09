package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Mode;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Training;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Override
    public Page<Training> getFilteredTrainings(
            String title,
            String organizer,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            Long provinceId,
            Long thematicId,
            Mode mode,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return trainingRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (organizer != null && !organizer.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("organizer")), "%" + organizer.toLowerCase() + "%"));
            }
            if (startDateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDateFrom));
            }
            if (startDateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), startDateTo));
            }
            if (provinceId != null) {
                predicates.add(cb.equal(root.get("province").get("id"), provinceId));
            }
            if (thematicId != null) {
                predicates.add(cb.equal(root.join("thematic").get("id"), thematicId));
            }
            if (mode != null) {
                predicates.add(cb.equal(root.get("mode"), mode));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public List<Training> getFilteredTrainingsList(
            String title,
            String organizer,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            Long provinceId,
            Long thematicId,
            Mode mode,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return trainingRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (organizer != null && !organizer.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("organizer")), "%" + organizer.toLowerCase() + "%"));
            }
            if (startDateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDateFrom));
            }
            if (startDateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), startDateTo));
            }
            if (provinceId != null) {
                predicates.add(cb.equal(root.get("province").get("id"), provinceId));
            }
            if (thematicId != null) {
                predicates.add(cb.equal(root.join("thematic").get("id"), thematicId));
            }
            if (mode != null) {
                predicates.add(cb.equal(root.get("mode"), mode));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, sort);
    }

    @Override
    public Training getTrainingById(Long id) throws Exception {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new Exception("Capacitación no encontrada con id: " + id));
    }

    @Override
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public Training updateTraining(Long id, Training training) {
        Training existing = trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found with id: " + id));
        existing.setTitle(training.getTitle());
        existing.setStartDate(training.getStartDate());
        existing.setDescription(training.getDescription());
        existing.setOrganizer(training.getOrganizer());
        existing.setProvince(training.getProvince());
        existing.setMode(training.getMode());
        return trainingRepository.save(existing);
    }

    @Override
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }
}
