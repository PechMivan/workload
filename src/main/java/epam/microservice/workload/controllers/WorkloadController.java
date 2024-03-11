package epam.microservice.workload.controllers;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workloads")
public class WorkloadController {


    @PostMapping
    public ResponseEntity<HttpStatus> modifyWorkload(ModifyWorkloadRequest request){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
