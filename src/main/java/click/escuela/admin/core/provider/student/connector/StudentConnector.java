package click.escuela.admin.core.provider.student.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.Connector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.StudentUpdateApi;
import click.escuela.admin.core.provider.student.dto.StudentDTO;

@Service
public class StudentConnector implements Connector<StudentDTO>{

	@Autowired
	private StudentController studentController;
	
	
	@Override
	public List<StudentDTO> getBySchool(String id) throws TransactionException {
		return studentController.getBySchool(id);
	}
	
	public void create(StudentApi studentApi) throws TransactionException{
		studentController.create(studentApi.getSchool(),studentApi);	
	}

	
	public void update( StudentUpdateApi studentUpdateApi) throws TransactionException{
		studentController.update(studentUpdateApi.getSchool(),studentUpdateApi);	
		}	

}
