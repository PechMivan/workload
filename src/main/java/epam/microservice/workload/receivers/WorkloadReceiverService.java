package epam.microservice.workload.receivers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.mappers.WorkloadMapper;
import epam.microservice.workload.services.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadReceiverService {

    private static final String WORKLOAD_QUEUE = "workload.queue";
    private static final String TRANSACTION_ID_HEADER = "Transaction-ID";
    private final WorkloadMapper workloadMapper;
    private final WorkloadService workloadService;

    @JmsListener(destination = WORKLOAD_QUEUE)
    public void updateWorkload(@Payload ModifyWorkloadRequest request,
                               @Header(name = TRANSACTION_ID_HEADER) String transactionId){
        log.info("received a message from {}", WORKLOAD_QUEUE);
        extractTransactionIdHeader(transactionId);
        log.info("Received a message from {} with {} action type",
                WORKLOAD_QUEUE,
                request.getActionType()
        );
        Workload workload = workloadMapper.modifyWorkloadRequestToWorkload(request);
        if (request.getActionType().equalsIgnoreCase("add")) workloadService.addHours(workload);
        else if (request.getActionType().equalsIgnoreCase("delete")) workloadService.removeHours(workload);
    }

    private void extractTransactionIdHeader(String transactionId){
        if (StringUtils.hasText(transactionId)) {
            MDC.put(TRANSACTION_ID_HEADER, transactionId);
        } else {
            MDC.put(TRANSACTION_ID_HEADER, UUID.randomUUID().toString());
        }
    }
}
