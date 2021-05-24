package click.escuela.admin.core.provider.student.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.TeacherConnector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.TeacherApi;

@Service
public class TeacherServiceImpl {

	@Autowired
	private TeacherConnector teacherConnector;

	public void create(String schoolId, TeacherApi teacherApi) throws TransactionException {
		teacherConnector.create(schoolId, teacherApi);
	}

	public void update(String schoolId, TeacherApi teacherApi) throws TransactionException {
		teacherConnector.update(schoolId, teacherApi);
	}

}
