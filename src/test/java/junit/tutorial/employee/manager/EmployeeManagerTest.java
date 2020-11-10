package junit.tutorial.employee.manager;

import junit.tutorial.employee.exception.IllegalNationalIdException;
import junit.tutorial.employee.exception.MisMatchingIdentityException;
import junit.tutorial.employee.manager.dto.EmployeeDTO;
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
    EmployeeManager manager;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private EmployeeDTO defaultEmployeeDto;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

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

        Mockito.doThrow(ServiceUnavailableException.class)
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
        Mockito.doReturn(person).when(mockedNationalIdVerifier).verify(anyString());
        Mockito.when(mockedNationalIdVerifier.verify("0010010017")).thenReturn(person);

        EmployeeDTO dto = new EmployeeDTO("0010010017", "modir", "jan", 2000.0);
        assertThrows(MisMatchingIdentityException.class, ()->manager.verifyEmployeeIdentity(dto));
    }

    @SneakyThrows
    @Test
    void addNewEmployee_WHEN_successful_THEN_returnTrue() {

        PersonDTO person = new PersonDTO("0010010017", "modir", "john", null);
        Mockito.doReturn(person).when(mockedNationalIdVerifier).verify(anyString());

        Mockito.doReturn(Optional.empty()).when(mockedRepository).findByNationalId(anyString());
        Mockito.doNothing().when(mockedRepository).save(any(Employee.class));

        assertTrue(manager.addNewEmployee(defaultEmployeeDto));

        InOrder inOrder1 = Mockito.inOrder(mockedNationalIdVerifier);
        InOrder inOrder2 = Mockito.inOrder(mockedRepository);
        inOrder1.verify(mockedNationalIdVerifier).verify(anyString());
        inOrder2.verify(mockedRepository).findByNationalId("0010010017");
        inOrder2.verify(mockedRepository).save(employeeCaptor.capture());
        inOrder1.verifyNoMoreInteractions();
        inOrder2.verifyNoMoreInteractions();

        Employee expectedEmp = new Employee("0010010017", "modir", "john", 2000.0);
        assertEquals(expectedEmp, employeeCaptor.getValue());
    }
}