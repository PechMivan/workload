package epam.microservice.workload.services;

import epam.microservice.workload.entities.Workload;

public interface WorkloadService {

    Workload getWorkloadByUsername(String username);

    void addHours(Workload workload);

    Workload createWorkload(Workload workload);

    void removeHours(Workload workload);
}
