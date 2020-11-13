package edu.java.studentorder.domain;

import org.omg.SendingContext.RunTime;

public enum StudentOrderStatus {
	START, CHECKED;
	
	public static StudentOrderStatus fromValue(int value) {
		for (StudentOrderStatus sos : StudentOrderStatus.values()) {
			if (sos.ordinal() == value) {
				return sos;
			}
		}
		
		throw new RuntimeException("Unknown value: " + value);
	}
}
