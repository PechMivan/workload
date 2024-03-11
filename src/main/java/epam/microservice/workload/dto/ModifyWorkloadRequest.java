package epam.microservice.workload.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ModifyWorkloadRequest {

    public String username;
    public String firstname;
    public String lastname;
    public boolean isActive;
    public String trainingDate;
    public int trainingDuration;
    public String actionType;
}
