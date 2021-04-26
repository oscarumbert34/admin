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

	public List<StudentDTO> getBySchool(String schoolId) throws TransactionException {
		return studentConnector.getBySchool(schoolId);
	}

	public StudentDTO getById(String schoolId, String studentId) throws TransactionException {
		return studentConnector.getById(schoolId, studentId);
	}

	public List<StudentDTO> getByCourse(String schoolId, String courseId) throws TransactionException {
		return studentConnector.getByCourse(schoolId, courseId);
	}

	public void create(StudentApi studentApi) throws TransactionException {
		studentConnector.create(studentApi);
	}

	public void update(StudentUpdateApi studentUpdateApi) throws TransactionException {
		studentConnector.update(studentUpdateApi);
	}

}
