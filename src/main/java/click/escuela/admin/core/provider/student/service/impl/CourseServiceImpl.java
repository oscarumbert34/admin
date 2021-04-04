package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.connector.StudentConnector;
import click.escuela.admin.core.provider.student.dto.StudentDTO;

@Service
public class CourseServiceImpl {

	@Autowired
	private StudentConnector studentConnector;
	
	public List<StudentDTO> getByCourse(String courseId) throws TransactionException{
		return studentConnector.getBySchool(courseId);
	}
	
}
