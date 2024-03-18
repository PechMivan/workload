package epam.microservice.workload.mappers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Trainer;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.helpers.DateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class WorkloadMapper {

    private final TrainerMapper trainerMapper;

    public Workload modifyWorkloadRequestToWorkload(ModifyWorkloadRequest request){
        LocalDate date = DateHelper.parseDateString(request.getTrainingDate());
        Workload.WorkloadBuilder workload = Workload.builder();

        workload.year(String.valueOf(date.getYear()));
        workload.month(String.valueOf(date.getMonth()));
        workload.totalWorkingHours(request.getTrainingDuration());

        Trainer trainer = trainerMapper.modifyWorkloadRequestToTrainer(request);
        workload.trainer(trainer);

        return workload.build();
    }
}
