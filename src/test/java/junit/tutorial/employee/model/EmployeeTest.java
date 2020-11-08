package junit.tutorial.employee.model;

import junit.tutorial.employee.extention.OddDaysExecutionCondition;
import junit.tutorial.employee.model.util.OffDaysAggregator;
import junit.tutorial.employee.model.util.WorkingDaysConverter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import junit.tutorial.employee.model.util.WorkingDaysAggregator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(OddDaysExecutionCondition.class)
class EmployeeTest {

    private Employee defaultEmployee;

    @BeforeEach
    private void getDefaultEmployee() {
        defaultEmployee = new Employee("0010010017", "modir", "john", 2000.0);
    }

    @ParameterizedTest
//    @MethodSource("populateInvalidArgumentsForConstructor")
    @CsvFileSource(resources = "/employee/constructor-illegal-arguments.csv")
    void testConstructor_WHEN_illegalArgumentsPassed_THEN_throwsIllegalArgumentException
            (String nationalId,
             String firstName,
             String lastName,
             Double salary){

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Employee(nationalId, firstName, lastName, salary))
                .withNoCause();
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
        Employee e = defaultEmployee;
        assertEquals("0010010017", e.getNationalId());
        assertEquals("modir", e.getFirstName());
        assertEquals("john", e.getLastName());
        assertEquals(2000.0, e.getSalary());
        assertThat(e.getWorkingDays())
                .containsExactlyInAnyOrderElementsOf(Employee.DEFAULT_WORKING_DAYS);
    }

    @Test
    void setWorkingDays_WHEN_null_THEN_throwsIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(
                ()->defaultEmployee.setWorkingDays(null)
        );
    }

    @ParameterizedTest
//    @MethodSource("populateValidWorkingDays")
    @CsvFileSource(resources = "/employee/valid-working-days.csv")
    // 1- includes Thursday or not
    // 2- includes Friday or not
    // 3- includes null or not
    // 4- size of collection (0, 1-2, 3, greater than 3)
    // 5- includes duplicated elements or not
    void setWorkingDays_WHEN_workingDaysAreValid_THEN_newWorkingDaysSetSuccessfully(
            @AggregateWith(WorkingDaysAggregator.class) List<DayOfWeek> newWorkingDays){

        defaultEmployee.setWorkingDays(newWorkingDays);

        LocalDate day1 = findADayBeforeAndIncludedIn(LocalDate.now(), newWorkingDays);
        defaultEmployee.addOffDay(day1);
        LocalDate day2 = findADayAfterAndIncludedIn(LocalDate.now(), newWorkingDays);
        defaultEmployee.addOffDay(day2);
        LocalDate day3 = findADayAfterAndNotIncludedIn(LocalDate.now(), newWorkingDays);
        defaultEmployee.addOffDay(day3);

        assertThat(defaultEmployee.getWorkingDays())
                .containsAll(newWorkingDays)
                .containsOnlyOnceElementsOf(newWorkingDays);

        assertThat(defaultEmployee.getOffDays())
                .containsExactlyInAnyOrder(day1, day2);

    }

    private LocalDate findADayAfterAndNotIncludedIn(LocalDate date, List<DayOfWeek> days) {
        LocalDate d = date.plusDays(1);
        while (days.contains(d))
            d = d.plusDays(1);
        return d;
    }

    private LocalDate findADayAfterAndIncludedIn(LocalDate date, List<DayOfWeek> days) {
        return null;
    }

    private LocalDate findADayBeforeAndIncludedIn(LocalDate now, List<DayOfWeek> newWorkingDays) {
        return null;
    }

    @ParameterizedTest
    @MethodSource("populateInValidWorkingDays")
    void setWorkingDays_WHEN_workingDaysAreInvalid_THEN_throwsIllegalArgumentException(List<DayOfWeek> newWorkingDays){
        assertThrows(IllegalArgumentException.class, ()->defaultEmployee.setWorkingDays(newWorkingDays));
    }

    private static Stream<List<DayOfWeek>> populateValidWorkingDays(){
        return Stream.of(
                Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY),
                Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY),
                Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY)
        );
    }

    private static Stream<List<DayOfWeek>> populateInValidWorkingDays(){
        return Stream.of(
                Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
                Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
                Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, null),
                Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.SUNDAY)
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/employee/off-days.csv")
    void getTotalOffDaysIn(int expectedOffDays, int year,
                           @ConvertWith(WorkingDaysConverter.class) List<DayOfWeek> workingDays,
                           @AggregateWith(OffDaysAggregator.class) List<LocalDate> offDays) {

        defaultEmployee.setWorkingDays(workingDays);
        offDays.forEach(d->defaultEmployee.addOffDay(d));

        assertEquals(expectedOffDays, defaultEmployee.getTotalOffDaysIn(year));
    }
}