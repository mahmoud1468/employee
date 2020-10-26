package junit.tutorial.employee.model;

import junit.tutorial.employee.exception.DuplicatedNationalIdException;
import junit.tutorial.employee.exception.UnfairSalaryException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Company {

    private Employee manager;
    private List<Employee> employees = new ArrayList<>();

    public Company(Employee manger){
        if (manger == null)
            throw new IllegalArgumentException("Manager could not be null");
        this.manager = manger;
    }

    private Company(){}


    public void addNewEmployee(String nationalId, String firstName, String lastName, Double salary){
        addNewEmployee(new Employee(nationalId, firstName, lastName, salary));
    }

    public void addNewEmployee(Employee employee){
        checkIfNationalIdIsDuplicated(employee.getNationalId());
        checkSalaryFairness(employee.getSalary());
        employees.add(employee);
    }

    private void checkIfNationalIdIsDuplicated(String nationalId) {
        if (manager.getNationalId().equals(nationalId))
            throw new DuplicatedNationalIdException();
        if (findEmployeeByNationalId(nationalId).isPresent())
            throw new DuplicatedNationalIdException();
    }

    private void checkSalaryFairness(Double salary) {
        if (salary >= manager.getSalary())
            throw new UnfairSalaryException("Employee's salary could not be greater than his/her manager");
        if (employees.stream().anyMatch(e -> e.getSalary() >= 2.0 * salary))
            throw new UnfairSalaryException("Employee's salary could not be less than half of any other employees");
    }

    public Optional<Employee> findEmployeeByNationalId(String nationalId) {
        return employees.stream()
                .filter(e->e.getNationalId().equals(nationalId))
                .findFirst();
    }

}
