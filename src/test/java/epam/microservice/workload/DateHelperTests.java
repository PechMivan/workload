package epam.microservice.workload;

import epam.microservice.workload.helpers.DateHelper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class DateHelperTests {

    @Test
    void parseString_withValidFormat_returnsLocalDate(){
        // Arrange
        String dateString = "2012-10-05";

        // Act
        LocalDate date = DateHelper.parseDateString(dateString);

        // Assert
        assertNotNull(date);
        assertEquals(dateString, date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    @Test
    void parseString_withInvalidFormat_throwsDateTimeParseException(){
        // Arrange
        String dateString = "05-10-2019";

        // Act & Assert
        assertThrows(DateTimeParseException.class, () -> DateHelper.parseDateString(dateString));
    }

    @Test
    void parseString_withNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> DateHelper.parseDateString(null));
    }
}
