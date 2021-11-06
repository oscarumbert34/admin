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
	public List<StudentDTO> getBySchool(String id, Boolean fullDetail) throws TransactionException {
		return studentController.getBySchool(id, fullDetail);
	}

	public StudentDTO getById(String schoolId, String studentId, Boolean fullDetail) throws TransactionException {
		return studentController.getById(schoolId, studentId, fullDetail);
	}

	public List<StudentDTO> getByCourse(String schoolId, String courseId, Boolean fullDetail)
			throws TransactionException {
		return studentController.getByCourse(schoolId, courseId, fullDetail);
	}

	public StudentDTO create(String schoolId, StudentApi studentApi) throws TransactionException {
		return studentController.createStudent(schoolId, studentApi);
	}

	public void update(String schoolId, StudentApi studentApi) throws TransactionException {
		studentController.updateStudent(schoolId, studentApi);
	}

}
