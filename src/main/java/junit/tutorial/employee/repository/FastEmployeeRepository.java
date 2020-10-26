package junit.tutorial.employee.repository;

import junit.tutorial.employee.model.Employee;

import java.util.Optional;

public class FastEmployeeRepository implements EmployeeRepository {

    public static final int WAIT_IN_MILLIS = 20;

    @Override
    public void save(Employee employee) {
        try {
            Thread.sleep(WAIT_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Employee employee) {
        try {
            Thread.sleep(WAIT_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Employee> findByNationalId(String nationalId) {
        try {
            Thread.sleep(WAIT_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
