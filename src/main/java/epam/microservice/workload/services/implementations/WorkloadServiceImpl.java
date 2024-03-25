package epam.microservice.workload.services.implementations;

import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.entities.WorkloadMonth;
import epam.microservice.workload.entities.WorkloadYear;
import epam.microservice.workload.exceptions.customExceptions.NotFoundException;
import epam.microservice.workload.repositories.WorkloadMongoRepository;
import epam.microservice.workload.services.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class WorkloadServiceImpl implements WorkloadService {

    private final WorkloadMongoRepository workloadRepository;

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

        List<WorkloadYear> updatedYears = addHoursToMonth(existingWorkload, workload);
        existingWorkload.setYears(updatedYears);

        workloadRepository.save(existingWorkload);
    }

    private List<WorkloadYear> addHoursToMonth(Workload existingWorkload, Workload request){
        List<WorkloadYear> existingYears = existingWorkload.getYears();

        WorkloadYear requestedYear = request.getYears().get(0);
        WorkloadMonth requestedMonth = requestedYear.getMonths().get(0);
        String requestedYearString = requestedYear.getYear();
        String requestedMonthString = requestedMonth.getMonth();

        WorkloadYear existingWorkloadYear = createWorkloadYear(requestedYear);

        int yearIndex = findYearIndex(existingYears, requestedYearString).orElse(-1);
        if (yearIndex >= 0){
            existingWorkloadYear = existingYears.get(yearIndex);
        } else {
            existingYears.add(existingWorkloadYear);
            return  existingYears;
        }

        WorkloadMonth existingWorkloadMonth = createWorkloadMonth(requestedMonth);

        List<WorkloadMonth> existingMonths = existingWorkloadYear.getMonths();
        int monthIndex = findMonthIndex(existingMonths, requestedMonthString).orElse(-1);
        if(monthIndex >= 0){
            existingWorkloadMonth = existingMonths.get(monthIndex);
            int totalHours = existingWorkloadMonth.getHoursSummary() + requestedMonth.getHoursSummary();
            existingWorkloadMonth.setHoursSummary(totalHours);
            existingWorkloadYear.getMonths().set(monthIndex, existingWorkloadMonth);
        } else {
            existingWorkloadYear.getMonths().add(existingWorkloadMonth);
        }

        existingYears.set(yearIndex, existingWorkloadYear);
        return existingYears;
    }

    private Optional<Integer> findMonthIndex(List<WorkloadMonth> existingMonths, String requestedMonthString) {
        Integer index = 0;
        for (WorkloadMonth monthOnCheck : existingMonths){
            if(monthOnCheck.getMonth().equals(requestedMonthString)){
                return Optional.of(index);
            }
            index++;
        }
        return Optional.empty();
    }

    private Optional<Integer> findYearIndex(List<WorkloadYear> existingYears, String requestedYearString) {
        Integer index = 0;
        for (WorkloadYear yearOnCheck : existingYears){
            if(yearOnCheck.getYear().equals(requestedYearString)){
                return Optional.of(index);
            }
            index++;
        }
        return Optional.empty();
    }

    private WorkloadMonth createWorkloadMonth(WorkloadMonth requestedMonth) {
        return WorkloadMonth.builder()
                .month(requestedMonth.getMonth())
                .hoursSummary(requestedMonth.getHoursSummary())
                .build();
    }

    private WorkloadYear createWorkloadYear(WorkloadYear requestedYear) {
        return WorkloadYear.builder()
                .year(requestedYear.getYear())
                .months(requestedYear.getMonths())
                .build();
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

        List<WorkloadYear> updatedYears = removeHoursToMonth(existingWorkload, workload);
        existingWorkload.setYears(updatedYears);
        workloadRepository.save(existingWorkload);
    }

    private List<WorkloadYear> removeHoursToMonth(Workload existingWorkload, Workload request) {
        List<WorkloadYear> existingYears = existingWorkload.getYears();

        WorkloadYear requestedYear = request.getYears().get(0);
        WorkloadMonth requestedMonth = requestedYear.getMonths().get(0);
        String requestedYearString = requestedYear.getYear();
        String requestedMonthString = requestedMonth.getMonth();

        int yearIndex = findYearIndex(existingYears, requestedYearString)
                .orElseThrow(() -> new NotFoundException("Year doesn't exist"));
        WorkloadYear existingWorkloadYear = existingYears.get(yearIndex);

        List<WorkloadMonth> existingMonths = existingWorkloadYear.getMonths();
        int monthIndex = findMonthIndex(existingMonths, requestedMonthString)
                .orElseThrow(() -> new NotFoundException("Month doesn't exist"));
        WorkloadMonth existingWorkloadMonth = existingMonths.get(monthIndex);

        int totalHours = existingWorkloadMonth.getHoursSummary() - requestedMonth.getHoursSummary();
        if (totalHours > 0){
            existingWorkloadMonth.setHoursSummary(totalHours);
            existingWorkloadYear.getMonths().set(monthIndex, existingWorkloadMonth);
            existingYears.set(yearIndex, existingWorkloadYear);
        } else {
            existingWorkloadYear.getMonths().remove(monthIndex);
        }

        existingYears.set(yearIndex, existingWorkloadYear);

        if(existingWorkloadYear.getMonths().isEmpty()){
            existingYears.remove(yearIndex);
        }

        return existingYears;
    }

}
