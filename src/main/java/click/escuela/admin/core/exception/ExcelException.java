package click.escuela.admin.core.exception;

import click.escuela.admin.core.enumator.ExcelMessage;

public class ExcelException extends TransactionException {

	private static final long serialVersionUID = 1L;

	public ExcelException(ExcelMessage excelMessage) {
		super(excelMessage.getCode(), excelMessage.getDescription());
	}
}
