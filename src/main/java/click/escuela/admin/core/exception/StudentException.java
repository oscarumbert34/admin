package click.escuela.admin.core.exception;

import click.escuela.admin.core.enumator.StudentMessage;

public class StudentException extends TransactionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StudentException(StudentMessage studentMessage) {
		super(studentMessage.getCode() ,studentMessage.getDescription());
	}

}
