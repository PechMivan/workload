package epam.microservice.workload.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private DateHelper(){};
    private static final String FORMAT = "yyyy-MM-dd";

    public static LocalDate parseDateString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        return LocalDate.parse(dateString, formatter);
    }
}
