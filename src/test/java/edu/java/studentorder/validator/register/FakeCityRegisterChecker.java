package edu.java.studentorder.validator.register;

import edu.java.studentorder.exception.CityRegisterException;
import edu.java.studentorder.domain.Adult;
import edu.java.studentorder.domain.Child;
import edu.java.studentorder.domain.Person;
import edu.java.studentorder.domain.register.CityRegisterResponse;

public class FakeCityRegisterChecker implements CityRegisterChecker {
	
	public static final String GOOD_1 = "1000";
	public static final String GOOD_2 = "2000";
	public static final String BAD_1 = "1001";
	public static final String BAD_2 = "2001";
	public static final String ERROR_1 = "1002";
	public static final String ERROR_2 = "2002";
	public static final String ERROR_T_1 = "1003";
	public static final String ERROR_T_2 = "2003";
	
	public CityRegisterResponse checkPerson (Person person)
			throws CityRegisterException {
		CityRegisterResponse res = new CityRegisterResponse();
		if (person instanceof Adult) {
			Adult t = (Adult) person;
			String ps = t.getPassportSeries();
			//System.out.println(t.toString());
			
			if (ps.equals(GOOD_1) || ps.equals(GOOD_2)) {
				res.setTemporal(false);
				res.setRegistered(true);
			}
			
			if (ps.equals(BAD_1) || ps.equals(BAD_2)) {
				res.setRegistered(false);
			}
			if (ps.equals(ERROR_1) || ps.equals(ERROR_2)) {
				CityRegisterException ex = new CityRegisterException("1", "GRN ERROR " + ps);
				throw ex;
			}
		}
		
		if (person instanceof Child) {
			Child child = (Child) person;
			res.setTemporal(true);
			res.setRegistered(true);
		}
		System.out.println(res);
		return res;
	}
}
