package epam.microservice.workload.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LoggingServiceImpl implements LoggingService{

    @Override
    public void displayRequest(HttpServletRequest request, Object body) {
        String reqMessage = buildRequestMessage(request, body);
        log.info("Request: {}", reqMessage);
    }

    private String buildRequestMessage(HttpServletRequest request, Object body) {
        StringBuilder reqMessage = new StringBuilder();
        Map<String,String> parameters = getParameters(request);

        reqMessage.append("REQUEST ");
        reqMessage.append("method = [").append(request.getMethod()).append("]");
        reqMessage.append(" path = [").append(request.getRequestURI()).append("] ");

        if(!parameters.isEmpty()) {
            reqMessage.append(" parameters = [").append(parameters).append("] ");
        }

        if(!Objects.isNull(body)) {
            reqMessage.append(" body = [").append(body).append("]");
        }

        return reqMessage.toString();
    }

    @Override
    public void displayResponse(HttpServletRequest request, HttpServletResponse response, Object body) {
        if(request.getRequestURI().equalsIgnoreCase("/actuator/prometheus")){
            return;
        }

        String respMessage = buildResponseMessage(request, response, body);
        log.info("Response: {}",respMessage);
    }

    private String buildResponseMessage(HttpServletRequest request, HttpServletResponse response, Object body) {
        StringBuilder respMessage = new StringBuilder();
        Map<String,String> headers = getHeaders(response);
        respMessage.append("RESPONSE ");
        respMessage.append(" method = [").append(request.getMethod()).append("]");
        if(!headers.isEmpty()) {
            respMessage.append(" ResponseHeaders = [").append(headers).append("]");
        }
        if(!Objects.isNull(body)) {
            respMessage.append(" responseBody = [").append(body).append("]");
        }

        respMessage.append(" responseStatus = [").append(response.getStatus()).append("]");
        return respMessage.toString();
    }

    private Map<String,String> getHeaders(HttpServletResponse response) {
        Map<String,String> headers = new HashMap<>();
        Collection<String> headerMap = response.getHeaderNames();
        for(String str : headerMap) {
            headers.put(str,response.getHeader(str));
        }
        return headers;
    }

    private Map<String,String> getParameters(HttpServletRequest request) {
        Map<String,String> parameters = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            parameters.put(paramName,paramValue);
        }
        return parameters;
    }


}
