package click.escuela.admin.core.provider.student.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.BillConnector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.BillApi;

@Service
public class BillServiceImpl {

	@Autowired
	private BillConnector billConnector;

	public void create(String schoolId, String studentId, BillApi billApi) throws TransactionException {
		billConnector.create(schoolId,studentId, billApi);
	}

}
