package edu.java.studentorder.validator;

import edu.java.studentorder.domain.student.AnswerStudent;
import edu.java.studentorder.domain.StudentOrder;

public class AnswerStudentValidator {
	public AnswerStudent checkStudent(StudentOrder so) {
		System.out.println("Check Student is running");
		AnswerStudent answer = new AnswerStudent();
		return answer;
	}
}
