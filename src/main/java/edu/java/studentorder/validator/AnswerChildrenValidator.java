package edu.java.studentorder.validator;

import edu.java.studentorder.domain.children.AnswerChildren;
import edu.java.studentorder.domain.StudentOrder;

public class AnswerChildrenValidator {
	public AnswerChildren checkChildren(StudentOrder so) {
		System.out.println("Check Children is running");
		AnswerChildren answer = new AnswerChildren();
		return answer;
	}
}
