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
}
