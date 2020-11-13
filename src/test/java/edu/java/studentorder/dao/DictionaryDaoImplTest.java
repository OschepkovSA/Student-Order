package edu.java.studentorder.dao;

//import static org.junit.jupiter.api.Assertions.*;

import edu.java.studentorder.domain.CountryArea;
import edu.java.studentorder.domain.PassportOffice;
import edu.java.studentorder.domain.RegisterOffice;
import edu.java.studentorder.domain.Street;
import edu.java.studentorder.exception.DaoException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class DictionaryDaoImplTest {
	
	private static final Logger logger = LoggerFactory.getLogger(DictionaryDaoImplTest.class);
	
	@BeforeClass
	public static void startUp() throws Exception {
		DBInit.startUp();
	}
	
	
	@Test
	public void testStreet() throws DaoException {
		LocalDateTime dt = LocalDateTime.now();
		
		logger.info("TEST {} {} ", dt, dt);
		List<Street> d = new DictionaryDaoImpl().findStreets("улица");
		Assert.assertTrue(d.size() == 5);
	}
	
	@Test
	public void testPassportOffice() throws DaoException {
		List<PassportOffice> po = new DictionaryDaoImpl().findpassportOffices("010020000000");
		Assert.assertTrue(po.size() == 2);
	}
	
	@Test
	public void testRegisterOffice() throws DaoException {
		List<RegisterOffice> ro = new DictionaryDaoImpl().findregisterOffices("010020000000");
		Assert.assertTrue(ro.size() == 2);
	}
	
	@Test
	public void testCountryAreas() throws DaoException {
		List<CountryArea> ca1 = new DictionaryDaoImpl().findAreas("");
		Assert.assertTrue(ca1.size() == 2);
		List<CountryArea> ca2 = new DictionaryDaoImpl().findAreas("");
		Assert.assertTrue(ca2.size() == 2);
		List<CountryArea> ca3 = new DictionaryDaoImpl().findAreas("");
		Assert.assertTrue(ca3.size() == 2);
		List<CountryArea> ca4 = new DictionaryDaoImpl().findAreas("");
		Assert.assertTrue(ca4.size() == 2);
	}
	
}