package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.api.CourseApi;
import click.escuela.admin.core.connector.CourseConnector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.dto.CourseDTO;

@Service
public class CourseServiceImpl {

	@Autowired
	private CourseConnector courseConnector;

	public void create(CourseApi courseApi) throws TransactionException {
		courseConnector.create(courseApi);
	}

	public List<CourseDTO> findAll() throws TransactionException {
		return courseConnector.getAllCourses();
	}

	
}
