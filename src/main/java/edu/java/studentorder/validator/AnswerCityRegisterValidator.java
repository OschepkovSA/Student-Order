package edu.java.studentorder.validator;

import edu.java.studentorder.domain.Person;
import edu.java.studentorder.domain.register.AnswerCityRegister;
import edu.java.studentorder.domain.StudentOrder;
import edu.java.studentorder.domain.register.AnswerCityRegisterItem;
import edu.java.studentorder.exception.CityRegisterException;
import edu.java.studentorder.domain.Child;
import edu.java.studentorder.domain.register.CityRegisterResponse;
import edu.java.studentorder.validator.register.CityRegisterChecker;
import edu.java.studentorder.validator.register.RealCityRegisterChecker;

public class AnswerCityRegisterValidator {
	public static final String IN_CODE = "NO GRN";
	
	public String hostName;
	public String login;
	protected int port;
	public String password;
	
	private CityRegisterChecker personChecker;
	
	public AnswerCityRegisterValidator() {
		personChecker = new RealCityRegisterChecker();
	}
	
	public AnswerCityRegister checkCityRegister(StudentOrder so) {
		
		AnswerCityRegister answer = new AnswerCityRegister();
		answer.addItem(checkPerson(so.getHusbund()));
		answer.addItem(checkPerson(so.getWife()));
		for (Child child : so.getChildren()) {
			answer.addItem(checkPerson(child));
		}
		return answer;
	}
	
	private AnswerCityRegisterItem checkPerson(Person person) {
		AnswerCityRegisterItem.CityStatus status = null;
		AnswerCityRegisterItem.CityError error = null;
		
		try {
			CityRegisterResponse tmp = personChecker.checkPerson(person);
			status = tmp.isRegistered() ?
					AnswerCityRegisterItem.CityStatus.YES :
					AnswerCityRegisterItem.CityStatus.NO;
		} catch (CityRegisterException e) {
			e.printStackTrace(System.out);
			status = AnswerCityRegisterItem.CityStatus.ERROR;
			error = new AnswerCityRegisterItem.CityError(e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			status = AnswerCityRegisterItem.CityStatus.ERROR;
			error = new AnswerCityRegisterItem.CityError(IN_CODE, e.getMessage());
		}
		
		AnswerCityRegisterItem ans = new AnswerCityRegisterItem(status, person, error);
		
		return ans;
	}
}
