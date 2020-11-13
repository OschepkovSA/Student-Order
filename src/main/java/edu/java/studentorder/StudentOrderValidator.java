package edu.java.studentorder;

import edu.java.studentorder.dao.StudentOrderDaoImpl;
import edu.java.studentorder.domain.*;
import edu.java.studentorder.domain.children.AnswerChildren;
import edu.java.studentorder.domain.register.AnswerCityRegister;
import edu.java.studentorder.domain.student.AnswerStudent;
import edu.java.studentorder.domain.wedding.AnswerWedding;
import edu.java.studentorder.exception.DaoException;
import edu.java.studentorder.mail.MailSender;
import edu.java.studentorder.validator.AnswerChildrenValidator;
import edu.java.studentorder.validator.AnswerCityRegisterValidator;
import edu.java.studentorder.validator.AnswerStudentValidator;
import edu.java.studentorder.validator.AnswerWeddingValidator;

import java.util.List;

public class StudentOrderValidator {
	private AnswerCityRegisterValidator CityRegisterVal;
	private AnswerChildrenValidator ChildrenVal;
	private AnswerStudentValidator StudentVal;
	private AnswerWeddingValidator WeddingVal;
	private MailSender mailSender;
	
	public StudentOrderValidator() {
		CityRegisterVal = new AnswerCityRegisterValidator();
		ChildrenVal = new AnswerChildrenValidator();
		StudentVal = new AnswerStudentValidator();
		WeddingVal = new AnswerWeddingValidator();
		mailSender = new MailSender();
	}
	
	
	public static void main(String[] args) {
		StudentOrderValidator sov = new StudentOrderValidator();
		sov.checkAll();
	}
	
	public void checkAll() {
		try {
			List<StudentOrder> soList = readStudentOrders();
			
			for (StudentOrder s : soList) {
				checkOneOrder(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkOneOrder(StudentOrder so) {
		AnswerCityRegister cityAnswer = checkCityRegister(so);
		//AnswerChildren answerChildren = checkChildren(so);
		//AnswerStudent answerStudent = checkStudent(so);
		//AnswerWedding answerWedding = checkWedding(so);
		//sendMail(so);
	}
	
	public List<StudentOrder> readStudentOrders() throws DaoException {
		return new StudentOrderDaoImpl().getStudentOrders();
	}
	
	AnswerCityRegister checkCityRegister(StudentOrder so) {
		return CityRegisterVal.checkCityRegister(so);
	}
	
	AnswerWedding checkWedding(StudentOrder so) {
		return WeddingVal.checkWedding(so);
	}
	
	AnswerChildren checkChildren(StudentOrder so) {
		return ChildrenVal.checkChildren(so);
	}
	
	AnswerStudent checkStudent(StudentOrder so) {
		return StudentVal.checkStudent(so);
	}
	
	void sendMail(StudentOrder so) {
		mailSender.sendMail(so);
	}
	
	
}
