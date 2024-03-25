package epam.microservice.workload.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkloadMonth {
    private String month;
    private int hoursSummary;
}
