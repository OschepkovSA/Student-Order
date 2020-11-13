package edu.java.studentorder.dao;

import edu.java.studentorder.domain.StudentOrder;
import edu.java.studentorder.exception.DaoException;

import java.util.List;

public interface StudentOrderDao {
	
	Long saveStudentOrder(StudentOrder so) throws DaoException;
	
	List<StudentOrder> getStudentOrders() throws DaoException;
}
