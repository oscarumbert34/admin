package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.StudentUpdateApi;
import click.escuela.admin.core.provider.student.connector.StudentConnector;
import click.escuela.admin.core.provider.student.dto.StudentDTO;

@Service
public class StudentServiceImpl {

	@Autowired
	private StudentConnector studentConnector;
	
	public List<StudentDTO> getBySchool(String schoolId) throws TransactionException{
		return studentConnector.getBySchool(schoolId);
	}
	
	public void create(StudentApi studentApi)  throws TransactionException{
		studentConnector.create(studentApi);
	}

	public void update(StudentUpdateApi studentUpdateApi)throws TransactionException {
		studentConnector.update(studentUpdateApi);
	}
	
}
