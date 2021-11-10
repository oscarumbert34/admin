package click.escuela.admin.core.exception;

import click.escuela.admin.core.enumator.SchoolMessage;

public class SchoolException extends TransactionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1735460522477676511L;

	public SchoolException(SchoolMessage message) {
		super(message.getCode(), message.getDescription());
	}
	
}