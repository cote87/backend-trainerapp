package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Mode;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Training;

public interface TrainingService {

        Page<Training> getFilteredTrainings(
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
                        String sortDir);

        List<Training> getFilteredTrainingsList(
                        String title,
                        String organizer,
                        LocalDate startDateFrom,
                        LocalDate startDateTo,
                        Long provinceId,
                        Long thematicId,
                        Mode mode,
                        String sortBy,
                        String sortDir);

        Training getTrainingById(Long id) throws Exception;

        Training saveTraining(Training training);

        Training updateTraining(Long id, Training training);

        void deleteTraining(Long id);
}
