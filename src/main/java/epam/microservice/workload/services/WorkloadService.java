package epam.microservice.workload.services;

import epam.microservice.workload.entities.Workload;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

public interface WorkloadService {
    Workload getWorkloadByUsernameAndYearAndMonth(String username,
                                                  String year,
                                                  String month);

    List<Workload> getWorkloadsByUsernameAndMonth(String username,
                                                  String month);

    @Transactional
    void addWorkload(Workload workload);

    @Transactional
    Workload createWorkload(Workload workload);

    @Transactional
    void deletePartialWorkload(Workload workload);

    @Transactional
    void deleteWorkload(Workload workload);
}
