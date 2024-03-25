package epam.microservice.workload;

import epam.microservice.workload.dto.ModifyWorkloadRequest;
import epam.microservice.workload.mappers.TrainerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class TrainerMapperTests {

    @InjectMocks
    TrainerMapper trainerMapper;

    @Test
    void trainerMapper_withRequestData_returnsTrainer(){
        // Arrange
        ModifyWorkloadRequest request =
                new ModifyWorkloadRequest(
                        "username", "firstname",
                        "lastname", true,
                        "2024-05-15", 5, "Add"
                );

        // Act
        Trainer trainer = trainerMapper.modifyWorkloadRequestToTrainer(request);
        // Assert
        assertNotNull(trainer);
        assertEquals(request.getUsername(), trainer.getUsername());
        assertEquals(request.getFirstname(), trainer.getFirstname());
        assertEquals(request.getLastname(), trainer.getLastname());
        assertEquals(request.isActive(), trainer.isStatus());
    }
}
