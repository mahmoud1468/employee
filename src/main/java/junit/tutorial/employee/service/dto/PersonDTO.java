package junit.tutorial.employee.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    String nationalId;
    String firstName;
    String lastName;
    LocalDate birthDate;
}
