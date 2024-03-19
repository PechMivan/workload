package epam.microservice.workload.receivers;

import epam.microservice.workload.entities.Workload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadReceiverService {

    private static final String WORKLOAD_QUEUE = "workload.queue";

    @JmsListener(destination = WORKLOAD_QUEUE)
    public void receiveMessage(Workload workload){
        log.info("received a message");
        log.info("Message is == {}", workload);
    }
}
