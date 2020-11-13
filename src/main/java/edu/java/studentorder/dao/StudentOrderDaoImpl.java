package edu.java.studentorder.dao;

import edu.java.studentorder.config.Config;
import edu.java.studentorder.domain.*;
import edu.java.studentorder.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StudentOrderDaoImpl implements StudentOrderDao {
	
	private static final Logger logger = LoggerFactory.getLogger(StudentOrderDaoImpl.class);
	
	private static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
			"student_order_status, student_order_date, h_sur_name, h_given_name, " +
			"h_patronymic, h_date_of_birth, h_passport_seria, h_passport_number, " +
			"h_passport_date, h_passport_office_id, h_post_index, h_street_code, " +
			"h_building, h_extension, h_apartment, h_university_id, h_student_number, " +
			"w_sur_name, w_given_name, w_patronymic, w_date_of_birth, w_passport_seria, " +
			"w_passport_number, w_passport_date, w_passport_office_id, w_post_index, " +
			"w_street_code, w_building, w_extension, w_apartment, w_university_id, " +
			"w_student_number, marriage_certificate_id, register_office_id, marriage_date)" +
			"VALUES " +
			"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String INSERT_CHILD = "INSERT INTO jc_student_child(" +
			"student_order_id, c_sur_name, c_given_name, c_patronymic, " +
			"c_date_of_birth, c_certificate_number, c_certificate_date, c_register_office_id, " +
			"c_post_index, c_street_code, c_building, c_extension, c_apartment)" +
			"VALUES " +
			"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static final String SELECT_ORDERS =
			"SELECT so.*, ro.r_office_area_id, ro.r_office_name ," +
					"po_h.p_office_area_id as h_p_office_area_id, po_h.p_office_name as h_p_office_name, " +
					"po_w.p_office_area_id as w_p_office_area_id, po_w.p_office_name as w_p_office_name " +
					"FROM jc_student_order so INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
					"INNER JOIN jc_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
					"INNER JOIN jc_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
					"WHERE student_order_status = ? ORDER BY student_order_date " +
					"LIMIT ?";
	
	private static final String SELECT_CHILD =
			"SELECT soc.*, ro.r_office_area_id, ro.r_office_name " +
			"FROM jc_student_child soc " +
			"INNER JOIN jc_register_office ro ON ro.r_office_id = soc.c_register_office_id " +
			"WHERE soc.student_order_id IN ";
	
	private static final String SELECT_ORDERS_FULL =
			"SELECT so.*, ro.r_office_area_id, ro.r_office_name," +
					"po_h.p_office_area_id as h_p_office_area_id, " +
					"po_h.p_office_name as h_p_office_name, " +
					"po_w.p_office_area_id as w_p_office_area_id, " +
					"po_w.p_office_name as w_p_office_name, " +
					"soc.*, ro_c.r_office_area_id, ro_c.r_office_name " +
					"FROM jc_student_order so " +
					"INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
					"INNER JOIN jc_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
					"INNER JOIN jc_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
					"INNER JOIN jc_student_child soc ON soc.student_order_id = so.student_order_id " +
					"INNER JOIN jc_register_office ro_c ON ro_c.r_office_id = soc.c_register_office_id " +
					"WHERE student_order_status = ? " +
					"ORDER BY so.student_order_id " +
					"LIMIT ?";
	
	
	private Connection getConnection() throws SQLException {
		return ConnectionBuilder.getConnection();
	}
	
	@Override
	public Long saveStudentOrder(StudentOrder so) throws DaoException {
		Long result = -1L;
		logger.debug("SO: {}", so); // печать входных параметров в логгер
		try (Connection con = getConnection();
			 PreparedStatement statement = con.prepareStatement(INSERT_ORDER, new String[] {"student_order_id"})) {
			//ИНСЕРТ ОРДЕР - строка для таблицы, в которую мы вставляем, вторая - откуда будем брать
			
			con.setAutoCommit(false);
			try {
				//HEADER
				statement.setInt(1 , StudentOrderStatus.START.ordinal());
				statement.setTimestamp(2 , Timestamp.valueOf(LocalDateTime.now()));
				
				//HUSBUND AND WIFE
				setParamsForAdult(statement , so.getHusbund() , 3);
				setParamsForAdult(statement , so.getWife() , 18);
				
				//MARRIAGE
				statement.setString(33 , so.getMarriageCertificateId());
				statement.setLong(34 , so.getMarriageOffice().getOfficeId());
				statement.setDate(35 , java.sql.Date.valueOf(so.getMarriageDate()));
				
				
				statement.executeUpdate(); //исполнение команды для изменения данных CRUd
				
				ResultSet gkRs = statement.getGeneratedKeys();
				if (gkRs.next()) {
					result = gkRs.getLong(1); // возврат значения строки по номеру колонки которую создали только что
					
				}
				gkRs.close();
				
				saveChildren(con , so , result);
				con.commit();
			}
			catch (SQLException e) {
				con.rollback();
				throw e;
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
			
		}
		return result;
	}
	
	private void saveChildren(Connection con , StudentOrder so, Long soId) throws SQLException {
		try (PreparedStatement statement = con.prepareStatement(INSERT_CHILD)) {
			int counter = 0; // счетчик чтобы скидывать по частям
			for (Child child : so.getChildren()) {
				statement.setLong(1 , soId);
				setParamsForChild(statement , child);
				statement.addBatch(); // добавление записи в буффер
				counter = + 1;
				if (counter > 10000) {
					statement.executeBatch(); // добаление всех записей из буфера сразу, так быстрее
					counter = 0;
				}
			}
			if (counter > 0) {
				statement.executeBatch(); // добавление оставшейся части
				// все это повышает производительность в случае больших баз, но для 5-10 детей это не подойдет
				// нужно большое количество
			}
		}
	}
	
	private void setParamsForAdult(PreparedStatement statement , Adult adult , int start) throws SQLException {
		setParamsForPerson(statement , adult , start);
		statement.setString(start + 4, adult.getPassportSeries());
		statement.setString(start + 5, adult.getPassportNumber());
		statement.setDate(start + 6, java.sql.Date.valueOf(adult.getIssueDate()));
		statement.setLong(start + 7, adult.getPassportOffice().getOfficeId());
		setParamsForAddress(statement , adult , start);
		statement.setLong(start+13, adult.getUniversity().getUniversityId());
		statement.setString(start+14, adult.getStudentId());
	}
	
	private void setParamsForAddress(PreparedStatement statement , Person person , int start) throws SQLException {
		Address address = person.getAddress();
		statement.setString(start + 8, address.getPostCode());
		statement.setLong(start + 9, address.getStreet().getStreetCode());
		statement.setString(start + 10, address.getBuilding());
		statement.setString(start + 11, address.getExtension());
		statement.setString(start + 12, address.getAppartment());
	}
	
	private void setParamsForPerson(PreparedStatement statement , Person person , int start) throws SQLException {
		statement.setString(start, person.getSurName());
		statement.setString(start + 1, person.getGivenName());
		statement.setString(start + 2, person.getPatronymic());
		statement.setDate(start + 3, java.sql.Date.valueOf(person.getDateOfBirth()));
	}
	
	private void setParamsForChild(PreparedStatement statement , Child child) throws SQLException {
		setParamsForPerson(statement, child,2);
		statement.setString(6,child.getCertificateNumber());
		statement.setDate(7,java.sql.Date.valueOf(child.getIssueDate()));
		statement.setLong(8,child.getRegisterOffice().getOfficeId());
		setParamsForAddress(statement, child,1);
	}
	
	@Override
	public List<StudentOrder> getStudentOrders() throws DaoException {
		return getStudentOrdersOneSelect();
		//getStudentOrdersTwoSelect();
	}
	
	private List<StudentOrder> getStudentOrdersOneSelect() throws DaoException {
		List<StudentOrder> result = new LinkedList<>();
		
		try (Connection con = getConnection();
			 PreparedStatement statement = con.prepareStatement(SELECT_ORDERS_FULL)) {
			
			Map<Long, StudentOrder> maps = new HashMap<>();
			
			statement.setInt(1, StudentOrderStatus.START.ordinal());
			int limit = Integer.parseInt(Config.getProperty(Config.DB_LIMIT));
			statement.setInt(2, limit);
			ResultSet resultSet = statement.executeQuery();
			int counter = 0;
			while (resultSet.next()) {
				Long soId = resultSet.getLong("student_order_id");
				if (!maps.containsKey(soId)) {
					StudentOrder so = getFullStudentOrder(resultSet);
					
					result.add(so);
					maps.put(soId, so);
				}
				StudentOrder so = maps.get(soId);
				so.addChild(fillChild(resultSet, "c_"));
				counter++;
			}
			//без счетчика, в случае, если обработка LIMIT, а еще есть ребенок в позиции LIMIT + 1,
			// то последняя семья отбрасывается
			
			if (counter >=  limit) {
				result.remove(result.size()-1);
			}
			
			resultSet.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
		}
		
		return result;
	}
	
	private List<StudentOrder> getStudentOrdersTwoSelect() throws DaoException {
		List<StudentOrder> result = new LinkedList<>();
		
		try (Connection con = getConnection();
			 PreparedStatement statement = con.prepareStatement(SELECT_ORDERS)) {
			
			statement.setInt(1, StudentOrderStatus.START.ordinal());
			statement.setInt(2, Integer.parseInt(Config.getProperty(Config.DB_LIMIT)));
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				StudentOrder so = getFullStudentOrder(resultSet);
				
				result.add(so);
			}

			resultSet.close();
			findChildren(con,result);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		
		return result;
	}
	
	private StudentOrder getFullStudentOrder(ResultSet resultSet) throws SQLException {
		StudentOrder so = new StudentOrder();
		fillStudentOrder(resultSet , so);
		fillWedding(resultSet , so);
		
		so.setHusbund(fillAdult(resultSet , "h_"));
		so.setWife(fillAdult(resultSet , "w_"));
		return so;
	}
	
	private void findChildren(Connection con , List<StudentOrder> result) throws SQLException {
		String cl = "(" + result.stream().map(so -> String.valueOf(so.getStudentOrderId()))
				.collect(Collectors.joining(",")) + ")";
		//прибавочный кусок для того, чтобы выбирать студентордер айди
		//хз вообще
		
		Map<Long, StudentOrder> maps = result.stream().collect(Collectors.toMap(so -> so.getStudentOrderId() , so -> so));
		//создается новая мапа с ключом студент ордер айди и значением самой заявки SO
		
		try (PreparedStatement statement = con.prepareStatement(SELECT_CHILD + cl)) {
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Child ch = fillChild(resultSet, "c_");
				StudentOrder studentOrder = maps.get(resultSet.getLong("student_order_id"));
				studentOrder.addChild(ch);
			}
		}
		
	}
	
	private Child fillChild(ResultSet resultSet, String pref) throws SQLException {
		String surName = resultSet.getString(pref + "sur_name");
		String givenName = resultSet.getString(pref + "given_name");
		String patronymic = resultSet.getString(pref + "patronymic");
		LocalDate dateOfBirth = resultSet.getDate(pref + "date_of_birth").toLocalDate();
		
		Child child = new Child(surName, givenName, patronymic, dateOfBirth);
		
		child.setCertificateNumber(resultSet.getString(pref + "certificate_number"));
		child.setIssueDate(resultSet.getDate(pref + "certificate_date").toLocalDate());
		
		RegisterOffice registerOffice = new RegisterOffice();
		Long registerOfficeId = resultSet.getLong(pref + "register_office_id");
		String roArae = resultSet.getString("r_office_area_id");
		String areaName = resultSet.getString("r_office_name");
		registerOffice.setOfficeAreaId(roArae);
		registerOffice.setOfficeId(registerOfficeId);
		registerOffice.setOfficeName(areaName);
		
		child.setRegisterOffice(registerOffice);
		
		fillAddress(resultSet , pref , child);
		
		
		return  child;
	}
	
	private void fillAddress(ResultSet resultSet , String pref , Person person) throws SQLException {
		Address address = new Address();
		address.setPostCode(resultSet.getString(pref + "post_index"));
		Street street = new Street(resultSet.getLong(pref + "street_code"), "");
		address.setStreet(street);
		address.setBuilding(resultSet.getString(pref + "building"));
		address.setAppartment(resultSet.getString(pref + "apartment"));
		address.setExtension(resultSet.getString(pref + "extension"));
		person.setAddress(address);
	}
	
	private Adult fillAdult(ResultSet resultSet , String pref) throws SQLException {
		Adult adult = new Adult();
		adult.setSurName(resultSet.getString(pref + "sur_name"));
		adult.setGivenName(resultSet.getString(pref + "given_name"));
		adult.setPatronymic(resultSet.getString(pref + "patronymic"));
		adult.setDateOfBirth(resultSet.getDate(pref + "date_of_birth").toLocalDate());
		adult.setPassportSeries(resultSet.getString(pref + "passport_seria"));
		adult.setPassportNumber(resultSet.getString(pref + "passport_number"));
		adult.setIssueDate(resultSet.getDate(pref + "passport_date").toLocalDate());
		
		Long poId = resultSet.getLong(pref + "passport_office_id");
		String poArea = resultSet.getString(pref + "p_office_area_id");
		String poName = resultSet.getString(pref + "p_office_name");
		PassportOffice passportOffice = new PassportOffice(poId, poName, poArea);
		adult.setPassportOffice(passportOffice);
		
		fillAddress(resultSet , pref , adult);
		
		University university = new University(resultSet.getLong((pref + "university_id")),"");
		adult.setUniversity(university);
		adult.setStudentId(resultSet.getString(pref + "student_number"));
		
		
		return adult;
	}
	
	private void fillWedding(ResultSet resultSet , StudentOrder so) throws SQLException {
		so.setMarriageCertificateId(resultSet.getString("marriage_certificate_id"));
		so.setMarriageDate(resultSet.getDate("marriage_date").toLocalDate());
		
		Long roId = resultSet.getLong("register_office_id");
		String areaId = resultSet.getString("r_office_area_id");
		String areaName = resultSet.getString("r_office_name");
		RegisterOffice registerOffice = new RegisterOffice(roId, areaName,areaId);
		so.setMarriageOffice(registerOffice);
	}
	
	private void fillStudentOrder(ResultSet resultSet , StudentOrder so) throws SQLException {
		so.setStudentOrderId(resultSet.getLong("student_order_id"));
		so.setStudentOrderDate(resultSet.getTimestamp("student_order_date").toLocalDateTime());
		so.setStudentOrderStatus(StudentOrderStatus.fromValue(resultSet.getInt("student_order_status")));
	}
	
	

}
