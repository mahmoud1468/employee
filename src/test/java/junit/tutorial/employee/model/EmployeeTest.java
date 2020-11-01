package junit.tutorial.employee.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {

    @ParameterizedTest
    @MethodSource("populateInvalidArgumentsForConstructor")
    void testConstructor_WHEN_illegalArgumentsPassed_THEN_throwsIllegalArgumentException
            (String nationalId,
             String firstName,
             String lastName,
             Double salary){

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Employee(nationalId, firstName, lastName, salary))
                .withNoCause()
                .withMessage(null);

    }

    private static Stream<Arguments> populateInvalidArgumentsForConstructor(){
        return Stream.of(
                Arguments.of(null, "modir", "john", 4000.0),
                Arguments.of("0010010017", "mo  dir", "john", 4000.0),
                Arguments.of("0010010017", "modir", "@john", 4000.0),
                Arguments.of("0010010017", "modir", "@john", null)
        );
    }

    @Test
    void testConstructor_WHEN_argumentsAreValid(){
        Employee e = new Employee("0010010017", "modir", "john", 2000.0);
        assertEquals("0010010017", e.getNationalId());
        assertEquals("modir", e.getFirstName());
        assertEquals("john", e.getLastName());
        assertEquals(2000.0, e.getSalary());
        assertThat(e.getWorkingDays())
                .containsExactlyInAnyOrderElementsOf(Employee.DEFAULT_WORKING_DAYS);
    }

}