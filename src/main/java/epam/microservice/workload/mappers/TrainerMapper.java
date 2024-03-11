package epam.microservice.workload.mappers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Trainer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TrainerMapper {

    public Trainer modifyWorkloadRequestToTrainer(ModifyWorkloadRequest request){
        Trainer.TrainerBuilder trainer = Trainer.builder();
        trainer.username(request.username);
        trainer.firstname(request.firstname);
        trainer.lastname(request.lastname);
        trainer.status(request.isActive);
        trainer.workloads(new ArrayList<>());

        return trainer.build();
    }
}
