package click.escuela.admin.core.provider.student.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.Connector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.dto.StudentDTO;

@Service
public class StudentConnector implements Connector<StudentDTO> {

	@Autowired
	private StudentController studentController;

	@Override
	public List<StudentDTO> getBySchool(String id) throws TransactionException {
		return studentController.getBySchool(id);
	}

	public StudentDTO getById(String schoolId, String studentId) throws TransactionException {
		return studentController.getById(schoolId, studentId);
	}

	public List<StudentDTO> getByCourse(String schoolId, String courseId) throws TransactionException {
		return studentController.getByCourse(schoolId, courseId);
	}

	public void create(StudentApi studentApi) throws TransactionException {
		studentController.createStudent(String.valueOf(studentApi.getSchoolId()), studentApi);
	}

	public void update(StudentApi studentApi) throws TransactionException {
		studentController.updateStudent(String.valueOf(studentApi.getSchoolId()), studentApi);
	}

}
