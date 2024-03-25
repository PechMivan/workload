package epam.microservice.workload.mappers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.entities.WorkloadMonth;
import epam.microservice.workload.entities.WorkloadYear;
import epam.microservice.workload.helpers.DateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkloadMapper {

    public Workload modifyWorkloadRequestToWorkload(ModifyWorkloadRequest request){
        LocalDate date = DateHelper.parseDateString(request.getTrainingDate());
        List<WorkloadYear> yearList = new ArrayList<>();
        WorkloadYear year = createWorkloadYear(String.valueOf(date.getYear()));
        WorkloadMonth month = createWorkloadMonth(String.valueOf(date.getMonth()), request.getTrainingDuration());
        year.getMonths().add(month);
        yearList.add(year);

        Workload.WorkloadBuilder workload = Workload.builder();
        workload.username(request.getUsername());
        workload.firstname(request.getFirstname());
        workload.lastname(request.getLastname());
        workload.status(request.isActive());
        workload.years(yearList);

        return workload.build();
    }

    private WorkloadMonth createWorkloadMonth(String month, int trainingDuration) {
        WorkloadMonth workloadMonth = new WorkloadMonth();
        workloadMonth.setMonth(month);
        workloadMonth.setHoursSummary(trainingDuration);
        return  workloadMonth;
    }

    private WorkloadYear createWorkloadYear(String year){
        WorkloadYear workloadYear = new WorkloadYear();
        workloadYear.setYear(year);
        workloadYear.setMonths(new ArrayList<>());
        return  workloadYear;
    }
}
