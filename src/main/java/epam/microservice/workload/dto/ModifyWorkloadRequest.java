package epam.microservice.workload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ModifyWorkloadRequest {
    @NotBlank(message = "username cannot be null or blank.")
    public String username;
    @NotBlank(message = "firstname cannot be null or blank.")
    public String firstname;
    @NotBlank(message = "lastname cannot be null or blank.")
    public String lastname;
    public boolean isActive;
    @NotBlank(message = "training date cannot be null or blank.")
    public String trainingDate;
    @NotNull(message = "training duration cannot be null.")
    @Positive(message = "training duration must be greater than 0")
    public int trainingDuration;
    @NotBlank(message = "action type cannot be null or blank.")
    public String actionType;
}
