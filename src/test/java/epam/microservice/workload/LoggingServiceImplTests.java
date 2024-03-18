package epam.microservice.workload;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import epam.microservice.workload.logger.LoggingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class LoggingServiceImplTests {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private LoggingServiceImpl loggingService;

    private ListAppender<ILoggingEvent> logWatcher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        logWatcher = new ListAppender<>();
        logWatcher.start();
        ((Logger) LoggerFactory.getLogger(LoggingServiceImpl.class)).addAppender(logWatcher);
    }

    @AfterEach
    void teardown() {
        ((Logger) LoggerFactory.getLogger(LoggingServiceImpl.class)).detachAndStopAllAppenders();
    }

    @Test
    void logRequestMessage_withCompleteData_successful() {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Collections.singletonList("param")));
        when(request.getParameter("param")).thenReturn("value");

        // Act
        loggingService.displayRequest(request, "testBody");

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("/example/path"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("param=value"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
    }

    @Test
    void logRequestMessage_withoutParameters_logsMessageWithoutParameters() {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getParameterNames()).thenReturn(Collections.emptyEnumeration());

        // Act
        loggingService.displayRequest(request, "testBody");

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("/example/path"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("param=value"));
    }

    @Test
    void logRequestMessage_withoutBody_logsMessageWithoutBody() {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Collections.singletonList("param")));
        when(request.getParameter("param")).thenReturn("value");

        // Act
        loggingService.displayRequest(request, null);

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("/example/path"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("param=value"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
    }

    @Test
    void logRequestMessage_withoutParametersAndBody_logsMessageWithoutParametersAndBody() {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getParameterNames()).thenReturn(Collections.emptyEnumeration());

        // Act
        loggingService.displayRequest(request, null);

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("/example/path"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("param=value"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
    }

    @Test
    void logResponseMessage_withActuatorRequestURI_avoidsLog(){
        // Arrange
        when(request.getRequestURI()).thenReturn("/actuator/prometheus");

        // Act
        loggingService.displayResponse(request, response, "testBody");

        // Assert
        assertTrue(logWatcher.list.isEmpty());
    }

    @Test
    void logResponseMessage_withCompleteData_successful(){
        // Arrange
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getMethod()).thenReturn("GET");
        when(response.getHeaderNames()).thenReturn(Collections.singletonList("Content-Type"));
        when(response.getHeader("Content-Type")).thenReturn("application/json");
        when(response.getStatus()).thenReturn(200);
        // Act
        loggingService.displayResponse(request, response, "testBody");

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("200"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("Content-Type=application/json"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
    }

    @Test
    void logResponseMessage_withoutHeaders_logsMessageWithoutHeaders(){
        // Arrange
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getMethod()).thenReturn("GET");
        when(response.getHeaderNames()).thenReturn(Collections.emptyList());
        when(response.getStatus()).thenReturn(200);
        // Act
        loggingService.displayResponse(request, response, "testBody");

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("200"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("Content-Type=application/json"));
    }

    @Test
    void logResponseMessage_withoutBody_logsMessageWithoutBody(){
        // Arrange
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getMethod()).thenReturn("GET");
        when(response.getHeaderNames()).thenReturn(Collections.singletonList("Content-Type"));
        when(response.getHeader("Content-Type")).thenReturn("application/json");
        when(response.getStatus()).thenReturn(200);
        // Act
        loggingService.displayResponse(request, response, null);

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("200"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("Content-Type=application/json"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
    }

    @Test
    void logResponseMessage_withoutHeadersAndBody_logsMessageWithoutHeadersAndBody(){
        // Arrange
        when(request.getRequestURI()).thenReturn("/example/path");
        when(request.getMethod()).thenReturn("GET");
        when(response.getHeaderNames()).thenReturn(Collections.emptyList());
        when(response.getStatus()).thenReturn(200);
        // Act
        loggingService.displayResponse(request, response, null);

        // Assert
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("GET"));
        assertTrue(logWatcher.list.get(0).getFormattedMessage().contains("200"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("Content-Type=application/json"));
        assertFalse(logWatcher.list.get(0).getFormattedMessage().contains("testBody"));
    }
}
