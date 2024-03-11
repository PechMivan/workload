package epam.microservice.workload.controllers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.mappers.WorkloadMapper;
import epam.microservice.workload.services.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workloads")
@RequiredArgsConstructor
public class WorkloadController {

    private final WorkloadMapper workloadMapper;
    private final WorkloadService workloadService;

    @PostMapping
    public ResponseEntity<HttpStatus> modifyWorkload(ModifyWorkloadRequest request){
        Workload workload = workloadMapper.modifyWorkloadRequestToWorkload(request);
        if (request.actionType.equalsIgnoreCase("add")) workloadService.addWorkload(workload);
        if (request.actionType.equalsIgnoreCase("delete")) workloadService.deleteWorkload(workload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
