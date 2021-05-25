package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.TeacherConnector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;

@Service
public class TeacherServiceImpl {

	@Autowired
	private TeacherConnector teacherConnector;

	public void create(String schoolId, TeacherApi teacherApi) throws TransactionException {
		teacherConnector.create(schoolId, teacherApi);
	}

	public void update(String schoolId, TeacherApi teacherApi) throws TransactionException {
		teacherConnector.update(schoolId, teacherApi);
	}

	public TeacherDTO getById(String schoolId, String studentId) throws TransactionException {
		return teacherConnector.getById(schoolId, studentId);
	}

	public List<TeacherDTO> getBySchoolId(String schoolId) {
		return teacherConnector.getBySchoolId(schoolId);
	}

	public List<TeacherDTO> getByCourseId(String schoolId, String courseId) throws TransactionException {
		return teacherConnector.getByCourseId(schoolId, courseId);
	}

}
