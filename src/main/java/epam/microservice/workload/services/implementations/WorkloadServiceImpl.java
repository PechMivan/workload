package epam.microservice.workload.services.implementations;

import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.entities.WorkloadMonth;
import epam.microservice.workload.entities.WorkloadYear;
import epam.microservice.workload.exceptions.customExceptions.NotFoundException;
import epam.microservice.workload.repositories.WorkloadMongoRepository;
import epam.microservice.workload.services.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class WorkloadServiceImpl implements WorkloadService {

    private final WorkloadMongoRepository workloadRepository;
    private final TrainerServiceImpl trainerService;

    @Override
    public Workload getWorkloadByUsername(String username){
        return workloadRepository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public void addHours(Workload workload){
        Workload existingWorkload = getWorkloadByUsername(workload.getUsername());

        if (existingWorkload == null){
            createWorkload(workload);
            return;
        }

        List<WorkloadYear> updatedYears = updateWorkloadYears(existingWorkload, workload);
        existingWorkload.setYears(updatedYears);

        workloadRepository.save(existingWorkload);
    }

    private List<WorkloadYear> updateWorkloadYears(Workload existingWorkload, Workload request) {
        List<WorkloadYear> years = existingWorkload.getYears();
        // Get unique year value from request
        String requestedYear = request.getYears().get(0).getYear();
        boolean present = false;
        for (WorkloadYear yearOnCheck : years){
            if(yearOnCheck.getYear().equals(requestedYear)){
                List<WorkloadMonth> updatedMonths = updateWorkloadMonths(yearOnCheck, request.getYears().get(0));
                yearOnCheck.setMonths(updatedMonths);
                present = true;
                break;
            }
        }
        if(!present){
           years.add(request.getYears().get(0));
        }
        return years;
    }

    private List<WorkloadMonth> updateWorkloadMonths(WorkloadYear existingYear, WorkloadYear request) {
        List<WorkloadMonth> months = existingYear.getMonths();
        // Get unique month value from request
        String requestedMonth = request.getMonths().get(0).getMonth();
        boolean present = false;
        for(WorkloadMonth monthOnCheck : months){
            if(monthOnCheck.getMonth().equals(requestedMonth)){
                int updatedHours = request.getMonths().get(0).getHoursSummary() + monthOnCheck.getHoursSummary();
                monthOnCheck.setHoursSummary(updatedHours);
                present = true;
            }
        }
        if (!present){
            months.add(request.getMonths().get(0));
        }
        return  months;
    }


    @Override
    public Workload createWorkload(Workload workload) {
        return workloadRepository.save(workload);
    }

    @Override
    public void removeHours(Workload workload) {
        Workload existingWorkload = getWorkloadByUsername(workload.getUsername());

        if (existingWorkload == null){
            throw new NotFoundException(
                    String.format("Workload for username %s not found.", workload.getUsername())
            );
        }

        int totalWorkingHours = existingWorkload.getTotalWorkingHours() - workload.getTotalWorkingHours();
        if (totalWorkingHours > 0) {
            existingWorkload.setTotalWorkingHours(totalWorkingHours);
            workloadRepository.save(existingWorkload);
        } else deleteWorkload(existingWorkload);
    }

    @Override
    public void deleteWorkload(Workload workload){
        workloadRepository.delete(workload);
    }

}
