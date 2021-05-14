package click.escuela.admin.core.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.BillApi;

@Service
public class BillConnector {

	@Autowired
	private StudentController billController;

	public void create(String schoolId, String studentId, BillApi billApi) throws TransactionException {
		billController.createBill(schoolId, studentId, billApi);
	}

}
