package epam.microservice.workload.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkloadYear {
    private String year;
    private List<WorkloadMonth> months;
}
