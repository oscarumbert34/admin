package click.escuela.admin.core.exception;

import click.escuela.admin.core.enumator.ProcessMessage;

public class ProcessException extends TransactionException{

	private static final long serialVersionUID = 1L;

	public ProcessException(ProcessMessage processMessage) {
		super(processMessage.getCode(), processMessage.getDescription());
	}
}
