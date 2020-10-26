package junit.tutorial.employee.repository;

import junit.tutorial.employee.model.Employee;

import java.io.IOException;
import java.util.Optional;

public interface EmployeeRepository {

    void save(Employee employee) throws IOException;
    void delete(Employee employee);
    Optional<Employee> findByNationalId(String nationalId);
}
