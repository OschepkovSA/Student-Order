package edu.java.studentorder.dao;

import edu.java.studentorder.domain.CountryArea;
import edu.java.studentorder.domain.PassportOffice;
import edu.java.studentorder.domain.RegisterOffice;
import edu.java.studentorder.domain.Street;
import edu.java.studentorder.exception.DaoException;

import java.util.List;

interface DictionaryDao {
	public List<Street> findStreets(String pattern) throws DaoException;
	public List<PassportOffice> findpassportOffices(String areaId) throws DaoException;
	public List<RegisterOffice> findregisterOffices(String areaId) throws DaoException;
	public List<CountryArea> findAreas(String areaId) throws DaoException;
	
}
