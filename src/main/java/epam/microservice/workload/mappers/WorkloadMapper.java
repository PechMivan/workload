package epam.microservice.workload.mappers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Trainer;
import epam.microservice.workload.entities.Workload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class WorkloadMapper {

    private final TrainerMapper trainerMapper;

    public Workload modifyWorkloadRequestToWorkload(ModifyWorkloadRequest request){
        LocalDate date = parseDate(request.trainingDate);
        Workload.WorkloadBuilder workload = Workload.builder();

        workload.year(String.valueOf(date.getYear()));
        workload.month(String.valueOf(date.getMonth()));
        workload.totalWorkingHours(request.trainingDuration);

        Trainer trainer = trainerMapper.modifyWorkloadRequestToTrainer(request);
        workload.trainer(trainer);

        return workload.build();
    }

    public LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
}
