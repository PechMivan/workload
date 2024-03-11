package epam.microservice.workload.services;

import epam.microservice.workload.entities.Trainer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

public interface TrainerService {
    Trainer getTrainerByUsername(String username);

    @Transactional
    Trainer createTrainer(Trainer trainer);
}
