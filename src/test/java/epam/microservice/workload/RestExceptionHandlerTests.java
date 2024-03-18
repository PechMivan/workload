package epam.microservice.workload;

import epam.microservice.workload.exceptions.RestExceptionHandler;
import epam.microservice.workload.exceptions.apierror.ApiError;
import epam.microservice.workload.exceptions.customExceptions.InvalidActionTypeException;
import epam.microservice.workload.exceptions.customExceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeParseException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTests {

    @InjectMocks
    RestExceptionHandler restExceptionHandler;

    void handleException_withMethodArgumentNotValidException_returnsResponseEntity(){
        // Arrange
        HttpHeaders headers = mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = mock(WebRequest.class);
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class, RETURNS_DEEP_STUBS);
        when(ex.getBindingResult().getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("object1", "field1", "message1")));


        // Act
        ResponseEntity<Object> response = restExceptionHandler.handleMethodArgumentNotValid(ex, headers, status, request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void handleException_withNotFoundException_returnsResponseEntity(){
        // Arrange
        NotFoundException ex = mock(NotFoundException.class);

        // Act
        ResponseEntity<Object> response = restExceptionHandler.handleNotFoundException(ex);
        // Assert
        assertNotNull(response);
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ApiError.class, response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleException_withInvalidActionTypeException_returnsResponseEntity(){
        // Arrange
        InvalidActionTypeException ex = mock(InvalidActionTypeException.class);

        // Act
        ResponseEntity<Object> response = restExceptionHandler.handleInvalidActionTypeException(ex);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ApiError.class, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleException_withDateTimeParserException_returnsResponseEntity(){
        // Arrange
        DateTimeParseException ex = mock(DateTimeParseException.class);

        // Act
        ResponseEntity<Object> response = restExceptionHandler.handleDateTimeParseException(ex);
        // Assert
        assertNotNull(response);
        assertNotNull(response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ApiError.class, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
