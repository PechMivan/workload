package epam.microservice.workload;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.helpers.DateHelper;
import epam.microservice.workload.mappers.TrainerMapper;
import epam.microservice.workload.mappers.WorkloadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkloadMapperTests {

    @Mock
    TrainerMapper trainerMapper;

    @InjectMocks
    WorkloadMapper workloadMapper;

    @Test
    void workloadMapper_withRequestData_returnsWorkload(){
        // Arrange
        String dateString = "2024-05-10";
        ModifyWorkloadRequest request =
                new ModifyWorkloadRequest(
                        "username", "firstname",
                        "lastname", true,
                        dateString, 5, "Add"
                );
        LocalDate date = DateHelper.parseDateString(dateString);
        when(trainerMapper.modifyWorkloadRequestToTrainer(request)).thenReturn(new Trainer());

        // Act
        Workload workload = workloadMapper.modifyWorkloadRequestToWorkload(request);
        // Assert
        assertNotNull(workload);
        assertNotNull(workload.getTrainer());
        assertEquals(String.valueOf(date.getYear()), workload.getYear());
        assertEquals(String.valueOf(date.getMonth()), workload.getMonth());
        assertEquals(request.getTrainingDuration(), workload.getTotalWorkingHours());
    }
}
