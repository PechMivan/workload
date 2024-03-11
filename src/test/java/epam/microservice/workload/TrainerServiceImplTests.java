package epam.microservice.workload;

import epam.microservice.workload.entities.Trainer;
import epam.microservice.workload.repositories.TrainerRepository;
import epam.microservice.workload.services.implementations.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTests {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void getTrainer_withExistingUsername_ReturnsTrainer() {
        // Arrange
        String username = "testUsername";
        Trainer expectedTrainer = new Trainer();
        expectedTrainer.setUsername(username);
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(expectedTrainer));

        // Act
        Trainer result = trainerService.getTrainerByUsername(username);

        // Assert
        assertEquals(expectedTrainer, result);
    }

    @Test
    void getTrainer_withNonExistingUsername_ReturnsNull() {
        // Arrange
        String username = "nonExistingUsername";
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Trainer result = trainerService.getTrainerByUsername(username);

        // Assert
        assertNull(result);
    }

    @Test
    void testCreateTrainer() {
        // Arrange
        Trainer trainer = new Trainer();
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        // Act
        Trainer createdTrainer = trainerService.createTrainer(trainer);

        // Assert
        assertEquals(trainer, createdTrainer);
        verify(trainerRepository, times(1)).save(trainer);
    }

    // Additional tests can be added for edge cases, error scenarios, etc.
}
