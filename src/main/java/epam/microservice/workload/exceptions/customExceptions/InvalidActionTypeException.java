package epam.microservice.workload.exceptions.customExceptions;

public class InvalidActionTypeException extends RuntimeException{
    public  InvalidActionTypeException(String message){
        super(message);
    }
}
