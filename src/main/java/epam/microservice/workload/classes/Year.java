package epam.microservice.workload.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Year {
    private String label;
    private List<Month> months;

    public void addMonth(Month month){
        if(!months.contains(month)){
            months.add(month);
        }
    }
}
