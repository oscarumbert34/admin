package click.escuela.admin.core.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;

@Service
public class TeacherConnector {

	@Autowired
	private StudentController teacherController;

	public TeacherDTO create(String schoolId, TeacherApi teacherApi) throws TransactionException {
		return teacherController.createTeacher(schoolId, teacherApi);
	}

	public void update(String schoolId, TeacherApi teacherApi) throws TransactionException {
		teacherController.updateTeacher(schoolId, teacherApi);
	}

	public TeacherDTO getById(String schoolId, String studentId) throws TransactionException {
		return teacherController.getByTeacherId(schoolId, studentId);
	}
	
	public List<TeacherDTO> getBySchoolId(String schoolId) {
		return teacherController.getBySchoolId(schoolId);
	}

	public List<TeacherDTO> getByCourseId(String schoolId, String courseId){
		return teacherController.getByCourseId(schoolId, courseId);
	}
	
	public void addCourses(String schoolId, String idTeacher, List<String> listUUIDs) throws TransactionException {
		teacherController.addCourses(schoolId, idTeacher, listUUIDs);
	}

	public void deleteCourses(String schoolId, String idTeacher, List<String> listUUIDs) throws TransactionException {
		teacherController.deleteCourses(schoolId, idTeacher, listUUIDs);
	}

}
