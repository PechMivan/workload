package epam.microservice.workload.services;

import epam.microservice.workload.entities.Workload;

import java.util.List;

public interface WorkloadService {
    Workload getWorkloadByUsernameAndYearAndMonth(String username,
                                                  String year,
                                                  String month);

    List<Workload> getWorkloadsByUsernameAndMonth(String username,
                                                  String month);

    Workload getWorkloadByUsername(String username);

    void addHours(Workload workload);

    Workload createWorkload(Workload workload);

    void removeHours(Workload workload);

    void deleteWorkload(Workload workload);
}
