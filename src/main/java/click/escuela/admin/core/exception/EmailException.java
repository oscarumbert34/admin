package click.escuela.admin.core.exception;

import click.escuela.admin.core.enumator.EmailMessage;

public class EmailException extends TransactionException {
	
	private static final long serialVersionUID = 1L;

	public EmailException(EmailMessage emailMessage) {
		super(emailMessage.getCode(), emailMessage.getDescription());
	}

}
