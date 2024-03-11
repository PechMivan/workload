package epam.microservice.workload.exceptions.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError{
    private String object;
    private String field;
    private String message;

    ApiValidationError(String field, String message) {
        this.object = field;
        this.message = message;
    }
}
