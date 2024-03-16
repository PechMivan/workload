package epam.microservice.workload.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoggingService {

    void displayRequest(HttpServletRequest request, Object body);

    void displayResponse(HttpServletRequest request, HttpServletResponse response, Object body);
}
