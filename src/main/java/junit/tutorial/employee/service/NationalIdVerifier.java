package junit.tutorial.employee.service;

import junit.tutorial.employee.service.dto.PersonDTO;

import javax.naming.ServiceUnavailableException;

public interface NationalIdVerifier {

    /**
     * It receives a nationalId and verifies it against a Government service.
     * @param nationalId: the national id to be verified.
     * @return personal information (first name, last name, birth date) of the person owns this national id.
     * @throws ServiceUnavailableException : in case is not able to connect to the Government service
     * @throws IllegalArgumentException : in case input is not well-formed (null, or any string other than 10 digits
     * @throws junit.tutorial.employee.exception.IllegalNationalIdException : in case national id is not owned by anyone.
     */
    PersonDTO verify(String nationalId) throws ServiceUnavailableException;
}
