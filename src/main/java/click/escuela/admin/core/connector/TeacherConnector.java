package click.escuela.admin.core.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.TeacherApi;

@Service
public class TeacherConnector {

	@Autowired
	private StudentController teacherController;

	public void create(String schoolId, TeacherApi teacherApi) throws TransactionException {
		teacherController.createTeacher(schoolId, teacherApi);
	}

	public void update(String schoolId, TeacherApi teacherApi) throws TransactionException {
		teacherController.updateTeacher(schoolId, teacherApi);
	}
}
