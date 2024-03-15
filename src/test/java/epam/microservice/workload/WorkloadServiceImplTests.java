package epam.microservice.workload;

import epam.microservice.workload.entities.Trainer;
import epam.microservice.workload.entities.Workload;
import epam.microservice.workload.exceptions.customExceptions.NotFoundException;
import epam.microservice.workload.repositories.WorkloadRepository;
import epam.microservice.workload.services.implementations.TrainerServiceImpl;
import epam.microservice.workload.services.implementations.WorkloadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkloadServiceImplTests {

    private final String USERNAME = "testUsername";
    private final String YEAR = "2022";
    private final String MONTH = "January";

    @Mock
    List<Workload> workloads;

    @Mock
    private WorkloadRepository workloadRepository;

    @Mock
    private TrainerServiceImpl trainerService;

    @InjectMocks
    private WorkloadServiceImpl workloadService;

    Workload existingWorkload;
    Workload inputWorkload;
    Trainer trainer;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        this.existingWorkload = new Workload();

        this.trainer = new Trainer();
        trainer.setUsername(USERNAME);

        this.inputWorkload = new Workload();
        inputWorkload.setYear(YEAR);
        inputWorkload.setMonth(MONTH);
        inputWorkload.setTrainer(trainer);
    }

    @Test
    void getWorkload_withValidData_returnWorkload(){
        // Arrange
        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString())).thenReturn(Optional.of(existingWorkload));

        // Act
        Workload workload = workloadService.getWorkloadByUsernameAndYearAndMonth(USERNAME, MONTH, YEAR);

        // Assert
        assertNotNull(workload);
        verify(workloadRepository, times(1)).findByTrainerUsernameAndYearAndMonth(USERNAME, MONTH, YEAR);
    }

    @Test
    void getWorkload_withInvalidData_returnNull(){
        // Arrange
        String username = "invalidUsername";
        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString())).thenReturn(Optional.empty());

        // Act
        Workload workload = workloadService.getWorkloadByUsernameAndYearAndMonth(username, MONTH, YEAR);

        // Assert
        assertNull(workload);
        verify(workloadRepository, times(1)).findByTrainerUsernameAndYearAndMonth(username, MONTH, YEAR);
    }

    @Test
    void getAllWorkloads_withValidData_returnList(){
        // Arrange
        when(workloadRepository.findByTrainerUsernameAndMonth(anyString(),anyString())).thenReturn(workloads);
        // Act
        List<Workload> workloadList = workloadService.getWorkloadsByUsernameAndMonth(USERNAME, MONTH);
        // Assert
        assertEquals(workloadList, workloads);
        assertFalse(workloads.isEmpty());
        verify(workloadRepository, times(1)).findByTrainerUsernameAndMonth(USERNAME, MONTH);
    }

    @Test
    void getAllWorkloads_withInvalidData_returnEmptyList(){
        // Arrange
        String username = "invalidUsername";
        when(workloadRepository.findByTrainerUsernameAndMonth(anyString(),anyString())).thenReturn(Collections.emptyList());
        // Act
        List<Workload> workloadList = workloadService.getWorkloadsByUsernameAndMonth(username, MONTH);
        // Assert
        assertTrue(workloadList.isEmpty());
        verify(workloadRepository, times(1)).findByTrainerUsernameAndMonth(username, MONTH);
    }

    @Test
    void addWorkload_withNonExistingWorkload_successful(){
        // Arrange
        int newWorkingHours = 20;
        inputWorkload.setTotalWorkingHours(newWorkingHours);
        
        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
        when(workloadRepository.save(any(Workload.class))).thenReturn(inputWorkload);
        // Act
        workloadService.addHours(inputWorkload);
        // Assert
        assertEquals(newWorkingHours, inputWorkload.getTotalWorkingHours());

        verify(workloadRepository, times(1)).save(inputWorkload); // Only one for creating the workload before returning
    }

    @Test
    void addWorkload_withExistingWorkload_successful(){
        // Arrange
        int newWorkingHours = 20;
        int actualWorkingHours = 50;
        inputWorkload.setTrainer(trainer);
        inputWorkload.setTotalWorkingHours(newWorkingHours);
        existingWorkload.setTotalWorkingHours(actualWorkingHours);

        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString())).thenReturn(Optional.of(existingWorkload));
        when(workloadRepository.save(any(Workload.class))).thenReturn(existingWorkload);
        // Act
        workloadService.addHours(inputWorkload);
        // Assert
        assertEquals(newWorkingHours + actualWorkingHours, existingWorkload.getTotalWorkingHours());

        verify(workloadRepository, times(1)).save(existingWorkload); // 1 for saving workload hours only
    }

    @Test
    void createWorkload_withNonExistingTrainer_successful(){
        // Arrange
        when(trainerService.getTrainerByUsername(USERNAME)).thenReturn(null); // New trainer
        when(trainerService.createTrainer(any(Trainer.class))).thenReturn(trainer); // New trainer
        when(workloadRepository.save(inputWorkload)).thenReturn(inputWorkload);

        // Act
        Workload result = workloadService.createWorkload(inputWorkload);

        // Assert
        assertEquals(result.getTrainer(), inputWorkload.getTrainer());

        verify(trainerService, times(1)).getTrainerByUsername(USERNAME);
        verify(trainerService, times(1)).createTrainer(inputWorkload.getTrainer());
        verify(workloadRepository, times(1)).save(inputWorkload);
    }

    @Test
    void createWorkload_withExistingTrainer_successful(){
        // Arrange
        when(trainerService.getTrainerByUsername(USERNAME)).thenReturn(trainer); // Existing trainer
        when(workloadRepository.save(inputWorkload)).thenReturn(inputWorkload);

        // Act
        Workload result = workloadService.createWorkload(inputWorkload);

        // Assert
        assertEquals(result.getTrainer(), inputWorkload.getTrainer());

        verify(trainerService, times(1)).getTrainerByUsername(USERNAME);
        verify(trainerService, never()).createTrainer(inputWorkload.getTrainer());
        verify(workloadRepository, times(1)).save(inputWorkload);
    }

    @Test
    void removeHours_withNonExistingWorkload_throwsNotFoundException(){
        // Arrange
        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> workloadService.removeHours(inputWorkload));
        verify(workloadRepository, never()).save(any());
        verify(workloadRepository, never()).delete(any());
    }

    @Test
    void removeHours_withExistingWorkloadAndPositiveTotalWorkingHours_successful(){
        // Arrange
        existingWorkload.setTotalWorkingHours(20);
        inputWorkload.setTotalWorkingHours(15);
        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(existingWorkload));

        // Act
        workloadService.removeHours(inputWorkload);

        // Assert
        verify(workloadRepository, times(1)).save(existingWorkload);
        verify(workloadRepository, never()).delete(existingWorkload);
    }

    @Test
    void removeHours_withExistingWorkloadAndNegativeTotalWorkingHours_successful(){
        // Arrange
        existingWorkload.setTotalWorkingHours(20);
        inputWorkload.setTotalWorkingHours(25);
        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(existingWorkload));

        // Act
        workloadService.removeHours(inputWorkload);

        // Assert
        verify(workloadRepository, never()).save(existingWorkload);
        verify(workloadRepository, times(1)).delete(existingWorkload);
    }

    @Test
    void removeHours_withExistingWorkloadAndZeroTotalWorkingHours_successful(){
        // Arrange
        existingWorkload.setTotalWorkingHours(20);
        inputWorkload.setTotalWorkingHours(20);
        when(workloadRepository.findByTrainerUsernameAndYearAndMonth(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(existingWorkload));

        // Act
        workloadService.removeHours(inputWorkload);

        // Assert
        verify(workloadRepository, never()).save(existingWorkload);
        verify(workloadRepository, times(1)).delete(existingWorkload);
    }

    @Test
    void deleteWorkload_withWorkload_successful(){

        // Act
        workloadService.deleteWorkload(inputWorkload);

        // Assert
        verify(workloadRepository, times(1)).delete(inputWorkload);
    }

}
