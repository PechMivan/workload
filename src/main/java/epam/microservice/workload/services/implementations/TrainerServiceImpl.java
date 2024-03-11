package epam.microservice.workload.services.implementations;

import epam.microservice.workload.entities.Trainer;
import epam.microservice.workload.repositories.TrainerRepository;
import epam.microservice.workload.services.TrainerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Override
    public Trainer getTrainerByUsername(String username){
        return  trainerRepository.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public Trainer createTrainer(Trainer trainer){
        return trainerRepository.save(trainer);
    }

}
