package edu.java.studentorder.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentOrder {
	
	private long StudentOrderId;
	private Adult husbund;
	private Adult wife;
	private List<Child> children;
	private String marriageCertificateId;
	private LocalDate marriageDate;
	private RegisterOffice marriageOffice;
	private LocalDateTime studentOrderDate;
	private StudentOrderStatus studentOrderStatus;
	
	public LocalDateTime getStudentOrderDate() {
		return studentOrderDate;
	}
	
	public void setStudentOrderDate(LocalDateTime studentOrderDate) {
		this.studentOrderDate = studentOrderDate;
	}
	
	public StudentOrderStatus getStudentOrderStatus() {
		return studentOrderStatus;
	}
	
	public void setStudentOrderStatus(StudentOrderStatus studentOrderStatus) {
		this.studentOrderStatus = studentOrderStatus;
	}
	
	public long getStudentOrderId() {
		return StudentOrderId;
	}
	
	public void setStudentOrderId(long studentOrderId) {
		StudentOrderId = studentOrderId;
	}
	
	public Adult getHusbund() {
		return husbund;
	}
	
	public void setHusbund(Adult husbund) {
		this.husbund = husbund;
	}
	
	public Adult getWife() {
		return wife;
	}
	
	public void setWife(Adult wife) {
		this.wife = wife;
	}
	
	public String getMarriageCertificateId() {
		return marriageCertificateId;
	}
	
	public void setMarriageCertificateId(String marriageCertificateId) {
		this.marriageCertificateId = marriageCertificateId;
	}
	
	public LocalDate getMarriageDate() {
		return marriageDate;
	}
	
	public void setMarriageDate(LocalDate marriageDate) {
		this.marriageDate = marriageDate;
	}
	
	public RegisterOffice getMarriageOffice() {
		return marriageOffice;
	}
	
	public void setMarriageOffice(RegisterOffice marriageOffice) {
		this.marriageOffice = marriageOffice;
	}
	
	public List<Child> getChildren() {
		return children;
	}
	
	public void addChild(Child child) {
		if (children == null) {
			children = new ArrayList<>(5);
		}
		children.add(child);
	}
	
	@Override
	public String toString() {
		return "StudentOrder{" + "StudentOrderId=" + StudentOrderId
				+ ", husbund=" + husbund
				+ ", wife=" + wife
				+ ", children=" + children
				+ ", marriageCertificateId='" + marriageCertificateId + '\''
				+ ", marriageDate=" + marriageDate
				+ ", marriageOffice=" + marriageOffice
				+ ", studentOrderDate=" + studentOrderDate
				+ ", studentOrderStatus=" + studentOrderStatus + '}';
	}
}
