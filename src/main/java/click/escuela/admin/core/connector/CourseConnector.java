package click.escuela.admin.core.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.api.CourseApi;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.CourseController;
import click.escuela.admin.core.provider.student.dto.CourseDTO;


@Service
public class CourseConnector {
	
	@Autowired
	private CourseController courseController;
	
	public void create(CourseApi courseApi) throws TransactionException{
		 courseController.create("1234",courseApi);
	}
	
	public List<CourseDTO> getAllCourses() throws TransactionException{
		return courseController.getAllCourses("1234");
	}
	
	public void addStudent(String idCourse, String idStudent)throws TransactionException {
		courseController.addStudent("1234",idCourse, idStudent);
	}

	public void deleteStudent(String idCourse, String idStudent)throws TransactionException {
		courseController.deleteStudent("1234",idCourse, idStudent);
		
	}
}
