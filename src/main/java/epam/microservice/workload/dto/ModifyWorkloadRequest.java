package epam.microservice.workload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ModifyWorkloadRequest {
    @NotBlank(message = "username cannot be null or blank.")
    private String username;
    @NotBlank(message = "firstname cannot be null or blank.")
    private String firstname;
    @NotBlank(message = "lastname cannot be null or blank.")
    private String lastname;
    private boolean isActive;
    @NotBlank(message = "training date cannot be null or blank.")
    private String trainingDate;
    @NotNull(message = "training duration cannot be null.")
    @Positive(message = "training duration must be greater than 0")
    private int trainingDuration;
    @NotBlank(message = "action type cannot be null or blank.")
    private String actionType;
}
