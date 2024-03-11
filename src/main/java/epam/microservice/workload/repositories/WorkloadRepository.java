package epam.microservice.workload.repositories;

import epam.microservice.workload.entities.Workload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkloadRepository extends JpaRepository<Workload, Long> {

    Optional<Workload> findByTrainerUsernameAndYearAndMonth(
            String username, String year, String month
    );

    List<Workload> findByTrainerUsernameAndMonth(
            String username, String month
    );
}
