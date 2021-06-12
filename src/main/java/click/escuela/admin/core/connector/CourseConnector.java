package click.escuela.admin.core.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.CourseApi;

@Service
public class CourseConnector {

	@Autowired
	private StudentController courseController;

	public void create(String schoolId, CourseApi courseApi) throws TransactionException {
		courseController.createCourse(schoolId, courseApi);
	}

	public void addStudent(String schoolId, String idCourse, String idStudent) throws TransactionException {
		courseController.addStudent(schoolId, idCourse, idStudent);
	}

	public void deleteStudent(String schoolId, String idCourse, String idStudent) throws TransactionException {
		courseController.deleteStudent(schoolId, idCourse, idStudent);
	}

	public void addTeacher(String schoolId, String idCourse, String idTeacher) throws TransactionException {
		courseController.addTeacher(schoolId, idCourse, idTeacher);
	}

	public void deleteTeacher(String schoolId, String idCourse, String idTeacher) throws TransactionException {
		courseController.deleteTeacher(schoolId, idCourse, idTeacher);
	}
}
