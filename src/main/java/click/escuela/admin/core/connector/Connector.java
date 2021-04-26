package click.escuela.admin.core.connector;

import java.util.List;

import click.escuela.admin.core.exception.TransactionException;

public interface Connector<T> {
	public List<T> getBySchool(String id) throws TransactionException;
}
