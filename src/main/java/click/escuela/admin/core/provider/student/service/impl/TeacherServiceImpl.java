package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.TeacherConnector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.processor.service.impl.SecurityServiceImpl;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.api.UserApi;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;

@Service
public class TeacherServiceImpl {

	@Autowired
	private TeacherConnector teacherConnector;
	
	@Autowired
	private SecurityServiceImpl securityServiceImpl;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;

	public void create(String schoolId, TeacherApi teacherApi) throws TransactionException {
		TeacherDTO teacherDTO = teacherConnector.create(schoolId, teacherApi);
		UserApi userApi = securityServiceImpl.saveUser(teacherToUser(schoolId, teacherDTO));
		if (!Objects.isNull(userApi)) {
			emailServiceImpl.sendEmail(userApi.getPassword(), userApi.getUserName(), userApi.getEmail(), schoolId);
		}

	}

	private UserApi teacherToUser(String schoolId, TeacherDTO teacherDTO) {
		return UserApi.builder().name(teacherDTO.getName()).surname(teacherDTO.getSurname())
				.email(teacherDTO.getEmail()).schoolId(schoolId).role("TEACHER").userId(teacherDTO.getId()).build();
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

	public List<TeacherDTO> getByCourseId(String schoolId, String courseId){
		return teacherConnector.getByCourseId(schoolId, courseId);
	}

	public void addCourses(String schoolId, String idTeacher, List<String> listUUIDs) throws TransactionException {
		teacherConnector.addCourses(schoolId, idTeacher, listUUIDs);
	}

	public void deleteCourses(String schoolId, String idTeacher, List<String> listUUIDs) throws TransactionException {
		teacherConnector.deleteCourses(schoolId, idTeacher, listUUIDs);
	}
}
