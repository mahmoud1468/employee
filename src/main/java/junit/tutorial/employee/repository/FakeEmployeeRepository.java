package junit.tutorial.employee.repository;

import junit.tutorial.employee.exception.DatabaseConnectionException;
import junit.tutorial.employee.model.Employee;

import java.io.IOException;
import java.util.Optional;

public class FakeEmployeeRepository implements EmployeeRepository {

    @Override
    public void save(Employee employee) throws IOException {
        if ("0010010017".equals(employee.getNationalId())){
            throw new IOException("Could not read/write into database");
        }
        else if ("0010010025".equals(employee.getNationalId())){
            throw new DatabaseConnectionException("Could not connect to database");
        }
    }

    @Override
    public void delete(Employee employee) {

    }

    @Override
    public Optional<Employee> findByNationalId(String nationalId) {
        return Optional.empty();
    }
}
