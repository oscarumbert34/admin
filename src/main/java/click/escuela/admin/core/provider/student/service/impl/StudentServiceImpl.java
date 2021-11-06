package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.processor.service.impl.SecurityServiceImpl;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.UserApi;
import click.escuela.admin.core.provider.student.connector.StudentConnector;
import click.escuela.admin.core.provider.student.dto.ParentDTO;
import click.escuela.admin.core.provider.student.dto.StudentDTO;

@Service
public class StudentServiceImpl {

	@Autowired
	private StudentConnector studentConnector;
	
	@Autowired
	private SecurityServiceImpl securityServiceImpl;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;

	public List<StudentDTO> getBySchool(String schoolId, Boolean fullDetail) throws TransactionException {
		return studentConnector.getBySchool(schoolId, fullDetail);
	}

	public StudentDTO getById(String schoolId, String studentId, Boolean fullDetail) throws TransactionException {
		return studentConnector.getById(schoolId, studentId, fullDetail);
	}

	public List<StudentDTO> getByCourse(String schoolId, String courseId, Boolean fullDetail)
			throws TransactionException {
		return studentConnector.getByCourse(schoolId, courseId, fullDetail);
	}

	public void create(String schoolId, StudentApi studentApi) throws TransactionException, SchoolException {
		StudentDTO studentDTO = studentConnector.create(schoolId, studentApi);
		UserApi userStudentApi = securityServiceImpl.saveUser(studentToUser(schoolId, studentDTO));
		if(userStudentApi != null) {
			emailServiceImpl.sendEmail(userStudentApi.getPassword(), userStudentApi.getUserName(), userStudentApi.getEmail(), schoolId);
		}
		UserApi userParentApi = securityServiceImpl.saveUser(parentToUser(schoolId, studentDTO.getParent()));
		if(userParentApi != null) {
			emailServiceImpl.sendEmail(userParentApi.getPassword(), userParentApi.getUserName(), userParentApi.getEmail(), schoolId);
		}
	}

	private UserApi studentToUser(String schoolId, StudentDTO studentDTO) {
		UserApi userApi = UserApi.builder().name(studentDTO.getName()).surname(studentDTO.getSurname()).
				email(studentDTO.getEmail()).schoolId(schoolId).role("STUDENT").userId(studentDTO.getId()).build();
		return userApi;
	}
	
	private UserApi parentToUser(String schoolId, ParentDTO parentDTO) {
		UserApi userApi = UserApi.builder().name(parentDTO.getName()).surname(parentDTO.getSurname()).
				email(parentDTO.getEmail()).schoolId(schoolId).role("PARENT").userId(parentDTO.getId()).build();
		return userApi;
	}

	public void update(String schoolId, StudentApi studentApi) throws TransactionException {
		studentConnector.update(schoolId, studentApi);
	}

}
