package edu.java.studentorder.domain;

import java.time.LocalDate;

public class Adult extends Person {
	private String passportSeries;
	private String passportNumber;
	private LocalDate issueDate;
	private PassportOffice passportOffice;
	private University university;
	private String studentId;
	
	public Adult() {
	
	}
	
	public Adult(String surName , String givenName ,
				 String patronymic , LocalDate dateOfBirth) {
		super(surName , givenName , patronymic , dateOfBirth);
	}
	
	
	public String getPassportSeries() {
		return passportSeries;
	}
	
	public void setPassportSeries(String passportSeries) {
		this.passportSeries = passportSeries;
	}
	
	public String getPassportNumber() {
		return passportNumber;
	}
	
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	
	public LocalDate getIssueDate() {
		return issueDate;
	}
	
	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}
	
	public PassportOffice getPassportOffice() {
		return passportOffice;
	}
	
	public void setPassportOffice(PassportOffice passportOffice) {
		this.passportOffice = passportOffice;
	}
	
	public University getUniversity() {
		return university;
	}
	
	public void setUniversity(University university) {
		this.university = university;
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	@Override
	public String toString() {
		return "Adult{" + "passportSeries='" + passportSeries + '\''
				+ ", passportNumber='" + passportNumber + '\''
				+ ", issueDate=" + issueDate
				+ ", passportOffice=" + passportOffice
				+ ", university=" + university
				+ ", studentId='" + studentId + '\'' + "} " + super.toString();
	}
}
