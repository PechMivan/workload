package epam.microservice.workload.services.implementations;

import epam.microservice.workload.entities.Trainer;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.repositories.WorkloadRepository;
import epam.microservice.workload.services.WorkloadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
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
            existingWorkload = createWorkload(workload);
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
            workload.setTrainer(trainer);
        }

        return workloadRepository.save(workload);
    }

    @Transactional
    @Override
    public void deleteWorkload(Workload workload){
        Workload existingWorkload = getWorkloadByUsernameAndYearAndMonth(
                workload.getTrainer().getUsername(),
                workload.getYear(),
                workload.getMonth()
        );

        if (existingWorkload == null){
            return; //throw exception
        }

        workloadRepository.delete(existingWorkload);
    }


}
