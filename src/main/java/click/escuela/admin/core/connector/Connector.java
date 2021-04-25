package click.escuela.admin.core.connector;

import java.util.List;

import click.escuela.student.exception.TransactionException;

public interface Connector<T> {

	public List<T> getBySchool(String id) throws TransactionException;
}
