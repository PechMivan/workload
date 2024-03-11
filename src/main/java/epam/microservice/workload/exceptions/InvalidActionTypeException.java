package epam.microservice.workload.exceptions;

public class InvalidActionTypeException extends RuntimeException{
    public  InvalidActionTypeException(String message){
        super(message);
    }
}
