package edu.java.studentorder.domain;

public class RegisterOffice {
	private long officeId;
	private String officeName;
	private String officeAreaId;
	
	public RegisterOffice() {
	
	}
	
	public RegisterOffice(long officeId , String officeName , String officeAreaId) {
		this.officeId = officeId;
		this.officeName = officeName;
		this.officeAreaId = officeAreaId;
	}
	
	public long getOfficeId() {
		return officeId;
	}
	
	public void setOfficeId(long officeId) {
		this.officeId = officeId;
	}
	
	public String getOfficeName() {
		return officeName;
	}
	
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	public String getOfficeAreaId() {
		return officeAreaId;
	}
	
	public void setOfficeAreaId(String officeAreaId) {
		this.officeAreaId = officeAreaId;
	}
	
	@Override
	public String toString() {
		return "RegisterOffice{" + "officeId=" + officeId
				+ ", officeName='" + officeName + '\''
				+ ", officeAreaId='" + officeAreaId + '\'' + '}';
	}
}
