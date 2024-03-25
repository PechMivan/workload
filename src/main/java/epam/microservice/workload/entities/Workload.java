package epam.microservice.workload.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "workloads")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Workload {
    @Id
    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private boolean status;
    private List<WorkloadYear> years;
}
