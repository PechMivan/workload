package epam.microservice.workload;

import epam.microservice.workload.exceptions.apierror.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTests {

    @Test
    void addValidationError_withNonEmptyList_successful(){
        // Arrange
        FieldError fieldError1 = new FieldError("objectTest", "field1", "error1");
        FieldError fieldError2 = new FieldError("objectTest", "field2", "error2");
        FieldError fieldError3 = new FieldError("objectTest", "field3", "error3");
        List<FieldError> subErrors = new ArrayList<>();
        subErrors.add(fieldError1);
        subErrors.add(fieldError2);
        subErrors.add(fieldError3);
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST);

        // Act
        error.addValidationErrors(subErrors);

        // Assert
        assertNotNull(error.getSubErrors());
        assertFalse(error.getSubErrors().isEmpty());
    }
}
