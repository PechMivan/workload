package epam.microservice.workload.controllers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.exceptions.InvalidActionTypeException;
import epam.microservice.workload.mappers.WorkloadMapper;
import epam.microservice.workload.services.WorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workloads")
@RequiredArgsConstructor
@Validated
public class WorkloadController {

    private final WorkloadMapper workloadMapper;
    private final WorkloadService workloadService;

    @PostMapping
    public ResponseEntity<HttpStatus> updateWorkload(@RequestBody @Valid ModifyWorkloadRequest request){
        Workload workload = workloadMapper.modifyWorkloadRequestToWorkload(request);
        if (request.getActionType().equalsIgnoreCase("add")) workloadService.addHours(workload);
        else if (request.getActionType().equalsIgnoreCase("delete")) workloadService.removeHours(workload);
        else throw new InvalidActionTypeException("Invalid action type. Accepted actions: add / delete.");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
