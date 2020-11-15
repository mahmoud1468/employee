package junit.tutorial.employee.manager;

import junit.tutorial.employee.exception.IllegalNationalIdException;
import junit.tutorial.employee.exception.MisMatchingIdentityException;
import junit.tutorial.employee.manager.dto.EmployeeDTO;
import junit.tutorial.employee.manager.util.MyAnswer;
import junit.tutorial.employee.model.Employee;
import junit.tutorial.employee.repository.EmployeeRepository;
import junit.tutorial.employee.service.NationalIdVerifier;
import junit.tutorial.employee.service.dto.PersonDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.ServiceUnavailableException;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeManagerTest {

    @Mock
    EmployeeRepository mockedRepository;

    @Mock
    NationalIdVerifier mockedNationalIdVerifier;

    @InjectMocks
    @Spy
    EmployeeManager manager;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private EmployeeDTO defaultEmployeeDto;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

    private void assertEmployeeEqualsToDTO(Employee employee, EmployeeDTO dto) {
        assertThat(employee)
                .usingRecursiveComparison()
                .ignoringFields("workingDays", "offDays", "creationTime")
                .isEqualTo(dto);
    }

    private void verifyInteractionsForAddNewEmployeeWhenSuccessful() throws IOException {
        InOrder inOrder1 = Mockito.inOrder(mockedNationalIdVerifier);
        InOrder inOrder2 = Mockito.inOrder(mockedRepository);
        inOrder2.verify(mockedRepository).findByNationalId("0010010017");
        inOrder2.verify(mockedRepository).save(employeeCaptor.capture());
        inOrder1.verifyNoMoreInteractions();
        inOrder2.verifyNoMoreInteractions();
    }

    private MockedConstruction<Employee> mockEmployeeConstructionWithCustomAnswer() {
        return mockConstruction(Employee.class, withSettings().defaultAnswer(new MyAnswer()));
    }

    @BeforeEach
    private void createDefaultEmployeeDTO() {
        defaultEmployeeDto = new EmployeeDTO("0010010017", "modir", "john", 2000.0);
    }

    @Test
    void test_injectMocks(){
        assertNotNull(manager.getEmployeeRepo());
        assertNotNull(manager.getNationalIdVerifier());
    }

    @SneakyThrows
    @Test
    void verifyEmployeeIdentity_WHEN_serviceIsUnavailable_THEN_throwsInternalError() {

        doThrow(ServiceUnavailableException.class)
                .doThrow(IllegalNationalIdException.class)
                .when(mockedNationalIdVerifier).verify(anyString());

        assertThrows(InternalError.class, ()->manager.verifyEmployeeIdentity(defaultEmployeeDto));

        Mockito.verify(mockedNationalIdVerifier, times(1)).verify(stringCaptor.capture());
        assertThat(stringCaptor.getAllValues()).containsExactly("0010010017");
        Mockito.verifyNoMoreInteractions(mockedNationalIdVerifier);
        Mockito.verifyNoInteractions(mockedRepository);
    }

    @SneakyThrows
    @Test
    void verifyEmployeeIdentity_WHEN_firstNamesMisMatch_THEN_throwsMisMatchingIdentityException(){

        PersonDTO person = new PersonDTO("0010010017", "modir", "john", null);
        doReturn(person).when(mockedNationalIdVerifier).verify(anyString());
        when(mockedNationalIdVerifier.verify("0010010017")).thenReturn(person);

        EmployeeDTO dto = new EmployeeDTO("0010010017", "modir", "jan", 2000.0);
        assertThrows(MisMatchingIdentityException.class, ()->manager.verifyEmployeeIdentity(dto));
    }

    @SneakyThrows
    @Test
    void addNewEmployee_WHEN_successful_THEN_returnTrue() {

            doNothing().when(manager).verifyEmployeeIdentity(any());
            doReturn(Optional.empty()).when(mockedRepository).findByNationalId(anyString()); //can remove (default answer)
            doNothing().when(mockedRepository).save(any(Employee.class)); //can remove (default behaviour)

            boolean result = manager.addNewEmployee(defaultEmployeeDto);

            assertTrue(result);
            verifyInteractionsForAddNewEmployeeWhenSuccessful();
            assertEmployeeEqualsToDTO(employeeCaptor.getValue(), defaultEmployeeDto);

    }

    @SneakyThrows
    @Test
    void mockNewlyConstructedObjects() {

        try(MockedConstruction<Employee> employeeMockedConstruction = mockConstruction(Employee.class)) {

            doNothing().when(manager).verifyEmployeeIdentity(any());
            doReturn(Optional.empty()).when(mockedRepository).findByNationalId(anyString());
            doNothing().when(mockedRepository).save(any(Employee.class));

            boolean result = manager.addNewEmployee(defaultEmployeeDto);

            assertTrue(result);
            verifyInteractionsForAddNewEmployeeWhenSuccessful();
            List<Employee> constructedEmployees = employeeMockedConstruction.constructed();
            assertEquals(1, constructedEmployees.size());
        }
    }

    @SneakyThrows
    @Test
    void mockWithCustomAnswers (){

        try(MockedConstruction<Employee> employeeMockedConstruction = mockEmployeeConstructionWithCustomAnswer()) {

            doNothing().when(manager).verifyEmployeeIdentity(any());
            doReturn(Optional.empty()).when(mockedRepository).findByNationalId(anyString());
            doAnswer(invocation -> {
                Thread.sleep(1000);
                return null;
            }).when(mockedRepository).save(any());

            assertTimeout(Duration.ofMillis(3000), ()->manager.addNewEmployee(defaultEmployeeDto));

        }

    }
}