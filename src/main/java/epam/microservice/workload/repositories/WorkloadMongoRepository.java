package epam.microservice.workload.repositories;

import epam.microservice.workload.entities.Workload;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WorkloadMongoRepository extends MongoRepository<Workload, Long> {

    Optional<Workload> findByUsername(String username);
}
