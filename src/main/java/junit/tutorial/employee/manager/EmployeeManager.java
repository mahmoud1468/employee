package junit.tutorial.employee.manager;

import junit.tutorial.employee.exception.DuplicatedNationalIdException;
import junit.tutorial.employee.exception.IllegalNationalIdException;
import junit.tutorial.employee.exception.MisMatchingIdentityException;
import junit.tutorial.employee.manager.dto.EmployeeDTO;
import junit.tutorial.employee.model.Employee;
import junit.tutorial.employee.repository.EmployeeRepository;
import junit.tutorial.employee.service.NationalIdVerifier;
import junit.tutorial.employee.service.dto.PersonDTO;
import junit.tutorial.employee.util.StringUtil;
import lombok.Setter;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class EmployeeManager {

    private EmployeeRepository employeeRepo;

    private NationalIdVerifier nationalIdVerifier;

    public EmployeeManager(EmployeeRepository employeeRepository, NationalIdVerifier nationalIdVerifier){
        this.employeeRepo = employeeRepository;
        this.nationalIdVerifier = nationalIdVerifier;
    }

    public EmployeeManager(){
    }

    public EmployeeManager(EmployeeRepository employeeRepository){
        this.employeeRepo = employeeRepository;
    }

    public boolean addNewEmployee(EmployeeDTO employeeDTO) throws IOException {
        validateEmployeeDTO(employeeDTO);
        Employee e = createEmployeeFrom(employeeDTO);
        verifyEmployeeIdentity(employeeDTO);
        checkIfEmployeeDoesNotExist(employeeDTO);
        employeeRepo.save(e);
        return true;
    }

    private void validateEmployeeDTO(EmployeeDTO employeeDTO) {
        if (Objects.isNull(employeeDTO))
            throw new IllegalArgumentException();
    }

    private Employee createEmployeeFrom(EmployeeDTO employeeDTO) {
        return new Employee(employeeDTO.getNationalId(), employeeDTO.getFirstName(),
                employeeDTO.getLastName(), employeeDTO.getSalary());
    }

    private void checkIfEmployeeDoesNotExist(EmployeeDTO employeeDTO) {
        employeeRepo.findByNationalId(employeeDTO.getNationalId())
                .ifPresent(e->{throw new DuplicatedNationalIdException();});
    }

    public void verifyEmployeeIdentity(EmployeeDTO employeeDTO) {
        try {
            PersonDTO person = nationalIdVerifier.verify(employeeDTO.getNationalId());
            if (!person.getFirstName().equals(employeeDTO.getFirstName()))
                throw new MisMatchingIdentityException();
            if (!person.getLastName().equals(employeeDTO.getLastName()))
                throw new MisMatchingIdentityException();
        }
        catch (ServiceUnavailableException e){
            throw new InternalError(e);
        }
    }

    public EmployeeDTO findEmployeeByNationalId(String nationalId){
        if (!StringUtil.isNationalId(nationalId))
            throw new IllegalNationalIdException();
        Optional<Employee> byNationalId = employeeRepo.findByNationalId(nationalId);
        if (!byNationalId.isPresent())
            return null;
        return createEmployeeDTOFrom(byNationalId.get());
    }

    private EmployeeDTO createEmployeeDTOFrom(Employee employee) {
        return EmployeeDTO.builder()
                .nationalId(employee.getNationalId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .salary(employee.getSalary())
                .build();
    }

}
