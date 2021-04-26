package click.escuela.admin.core.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.api.CourseApi;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;

@Service
public class CourseConnector {
	
	@Autowired
	private StudentController courseController;
	
	public void create(String schoolId, CourseApi courseApi) throws TransactionException{
		 courseController.create(schoolId,courseApi);
	}
	
	public void addStudent(String idCourse, String idStudent)throws TransactionException {
		courseController.addStudent("1234",idCourse, idStudent);
	}

	public void deleteStudent(String idCourse, String idStudent)throws TransactionException {
		courseController.deleteStudent("1234",idCourse, idStudent);
		
	}
}
