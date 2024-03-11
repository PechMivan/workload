package epam.microservice.workload.services.implementations;

import epam.microservice.workload.entities.Trainer;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.exceptions.NotFoundException;
import epam.microservice.workload.repositories.WorkloadRepository;
import epam.microservice.workload.services.WorkloadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class WorkloadServiceImpl implements WorkloadService {

    private final WorkloadRepository workloadRepository;
    private final TrainerServiceImpl trainerService;

    @Override
    public Workload getWorkloadByUsernameAndYearAndMonth(String username,
                                                         String year,
                                                         String month){
        return workloadRepository.findByTrainerUsernameAndYearAndMonth(username, year, month)
                .orElse(null);
    }

    @Override
    public List<Workload> getWorkloadsByUsernameAndMonth(String username,
                                                         String month){
        return workloadRepository.findByTrainerUsernameAndMonth(username, month);
    }

    @Transactional
    @Override
    public void addWorkload(Workload workload){
        Workload existingWorkload = getWorkloadByUsernameAndYearAndMonth(
                                        workload.getTrainer().getUsername(),
                                        workload.getYear(),
                                        workload.getMonth()
        );

        if (existingWorkload == null){
            createWorkload(workload);
            return;
        }

        existingWorkload.setTotalWorkingHours(existingWorkload.getTotalWorkingHours()
                                              + workload.getTotalWorkingHours()
        );
        workloadRepository.save(existingWorkload);
    }

    @Transactional
    @Override
    public Workload createWorkload(Workload workload) {
        Trainer trainer = trainerService.getTrainerByUsername(workload.getTrainer().getUsername());

        if(trainer == null){
            trainer = trainerService.createTrainer(workload.getTrainer());
        }

        workload.setTrainer(trainer);
        return workloadRepository.save(workload);
    }

    @Transactional
    @Override
    public void deletePartialWorkload(Workload workload){
        Workload existingWorkload = getWorkloadByUsernameAndYearAndMonth(
                workload.getTrainer().getUsername(),
                workload.getYear(),
                workload.getMonth()
        );

        if (existingWorkload == null){
            throw new NotFoundException(String.format("Workload with [year: %s | month: %s] for username %s not found.",
                                         workload.getYear(), workload.getMonth(), workload.getTrainer().getUsername()));
        }

        int totalWorkingHours = existingWorkload.getTotalWorkingHours() - workload.getTotalWorkingHours();
        if (totalWorkingHours > 0) {
            existingWorkload.setTotalWorkingHours(totalWorkingHours);
            workloadRepository.save(existingWorkload);
        } else deleteWorkload(existingWorkload);
    }

    @Transactional
    @Override
    public void deleteWorkload(Workload workload){
        workloadRepository.delete(workload);
    }

}
