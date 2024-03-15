package epam.microservice.workload.services;

import epam.microservice.workload.entities.Trainer;
import jakarta.transaction.Transactional;

public interface TrainerService {
    Trainer getTrainerByUsername(String username);

    @Transactional
    Trainer createTrainer(Trainer trainer);
}
