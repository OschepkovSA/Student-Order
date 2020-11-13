package edu.java.studentorder.validator.register;

import edu.java.studentorder.exception.CityRegisterException;
import edu.java.studentorder.domain.Person;
import edu.java.studentorder.domain.register.CityRegisterResponse;

public interface CityRegisterChecker {
	CityRegisterResponse checkPerson (Person person) throws CityRegisterException;
}
