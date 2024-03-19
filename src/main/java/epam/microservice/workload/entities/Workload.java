package epam.microservice.workload.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Workload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "year_")
    private String year;
    @Column(name = "month_")
    private String month;
    private int totalWorkingHours;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    Trainer trainer;

    @Override
    public String toString() {
        return "Workload{" +
                "id=" + id +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", totalWorkingHours=" + totalWorkingHours +
                ", trainer=" + trainer.toString() +
                '}';
    }
}
