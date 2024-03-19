package epam.microservice.workload.receivers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.mappers.WorkloadMapper;
import epam.microservice.workload.services.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadReceiverService {

    private static final String WORKLOAD_QUEUE = "workload.queue";
    private final WorkloadMapper workloadMapper;
    private final WorkloadService workloadService;

    @JmsListener(destination = WORKLOAD_QUEUE)
    public void updateWorkload(ModifyWorkloadRequest request){
        log.info("received a message from {}", WORKLOAD_QUEUE);
        Workload workload = workloadMapper.modifyWorkloadRequestToWorkload(request);
        if (request.getActionType().equalsIgnoreCase("add")) workloadService.addHours(workload);
        else if (request.getActionType().equalsIgnoreCase("delete")) workloadService.removeHours(workload);
    }
}
