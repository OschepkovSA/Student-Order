package edu.java.studentorder.dao;

import edu.java.studentorder.config.Config;
import edu.java.studentorder.domain.CountryArea;
import edu.java.studentorder.domain.PassportOffice;
import edu.java.studentorder.domain.RegisterOffice;
import edu.java.studentorder.domain.Street;
import edu.java.studentorder.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao {
	
	private static final Logger logger = LoggerFactory.getLogger(DictionaryDaoImpl.class);
	
	
	private static final String GET_STREET = "SELECT street_code, street_name FROM "
			+ "jc_street WHERE UPPER(street_name) LIKE UPPER(?)"; // язык SQL - ? - поиск по этому слову
	private static final String GET_PASSPORT = "SELECT * FROM "
			+ "jc_passport_office WHERE p_office_area_id = ?"; // язык SQL - ? - поиск по этому слову
	private static final String GET_REGISTER = "SELECT * FROM "
			+ "jc_register_office WHERE r_office_area_id = ?"; // язык SQL - ? - поиск по этому слову
	private static final String GET_AREA = "SELECT * FROM "
			+ "jc_country_struct WHERE area_id like ? and area_id<> ?"; // язык SQL - ? - поиск по этому слову <> кроме
	
	
	private Connection getConnection() throws SQLException {
		return ConnectionBuilder.getConnection();
	}
	
	public List<Street> findStreets(String pattern) throws DaoException {
		List<Street> result = new LinkedList<>();

		
		try (Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement(GET_STREET)) {
			statement.setString(1, "%" + pattern + "%"); // шаблон для поиска в месте где стоит ?
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Street str = new Street(rs.getLong("street_code") , rs.getString("street_name"));
				result.add(str);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
		}
		return result;
	}
	
	@Override
	public List<PassportOffice> findpassportOffices(String areaId) throws DaoException {
		List<PassportOffice> result = new LinkedList<>();
		try (Connection con = getConnection();
			 PreparedStatement statement = con.prepareStatement(GET_PASSPORT)) {
			statement.setString(1, areaId); // шаблон для поиска в месте где стоит ?
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				PassportOffice po = new PassportOffice(
						rs.getLong("p_office_id"),
						rs.getString("p_office_name"),
						rs.getString("p_office_area_id"));
				result.add(po);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
		}
		return result;
	}
	
	@Override
	public List<RegisterOffice> findregisterOffices(String areaId) throws DaoException {
		List<RegisterOffice> result = new LinkedList<>();
		try (Connection con = getConnection();
			 PreparedStatement statement = con.prepareStatement(GET_REGISTER)) {
			statement.setString(1, areaId); // шаблон для поиска в месте где стоит ?
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				RegisterOffice ro = new RegisterOffice(
						rs.getLong("r_office_id"),
						rs.getString("r_office_name"),
						rs.getString("r_office_area_id"));
				result.add(ro);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
		}
		return result;
	}
	
	@Override
	public List<CountryArea> findAreas(String areaId) throws DaoException {
		List<CountryArea> result = new LinkedList<>();
		
		
		try (Connection con = getConnection();
			 PreparedStatement statement = con.prepareStatement(GET_AREA)) {
			
			String param1 = buildParam(areaId);
			String param2 = areaId;
			statement.setString(1, param1); // шаблон для поиска в месте где стоит ?
			statement.setString(2,param2);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				CountryArea str = new CountryArea(
						rs.getString("area_id") ,
						rs.getString("area_name"));
				result.add(str);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
		}
		return result;
	}
	
	private String buildParam(String areaId) throws SQLException {
		if (areaId == null || areaId.trim().isEmpty()) {
			return "__0000000000";
		} else if (areaId.endsWith("0000000000")) {
			return areaId.substring(0, 2) + "___0000000";
		} else if (areaId.endsWith("0000000")) {
			return areaId.substring(0, 5) + "___0000";
		} else if (areaId.endsWith("0000")) {
			return areaId.substring(0,8) + "____";
		} throw new SQLException("Invalid parameter areaId: " + areaId);
	}
}
