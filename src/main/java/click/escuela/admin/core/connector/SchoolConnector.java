package click.escuela.admin.core.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.SchoolApi;

@Service
public class SchoolConnector {

	@Autowired
	private StudentController schoolController;

	public void create(SchoolApi schoolApi) throws TransactionException {
		schoolController.createSchool(schoolApi);
	}

}
