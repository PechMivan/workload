package epam.microservice.workload.mappers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TrainerMapper {

    public Trainer modifyWorkloadRequestToTrainer(ModifyWorkloadRequest request){
        Trainer.TrainerBuilder trainer = Trainer.builder();
        trainer.username(request.getUsername());
        trainer.firstname(request.getFirstname());
        trainer.lastname(request.getLastname());
        trainer.status(request.isActive());
        trainer.workloads(new ArrayList<>());

        return trainer.build();
    }
}
