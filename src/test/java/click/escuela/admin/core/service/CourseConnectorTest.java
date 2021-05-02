package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.admin.core.connector.CourseConnector;
import click.escuela.admin.core.enumator.CourseEnum;
import click.escuela.admin.core.enumator.StudentEnum;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.CourseApi;

@RunWith(MockitoJUnitRunner.class)
public class CourseConnectorTest {

	
	@Mock
	private StudentController courseController;
	
	private CourseConnector courseConnector = new CourseConnector();
	private CourseApi courseApi;
	private String schoolId;
	private String idStudent;
	private String idCourse;

	@Before
	public void setUp() throws TransactionException {

		schoolId = UUID.randomUUID().toString();
		idStudent = UUID.randomUUID().toString();
		idCourse = UUID.randomUUID().toString();
		
		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		ReflectionTestUtils.setField(courseConnector, "courseController", courseController);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			courseConnector.create(schoolId, courseApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws TransactionException {

		when(courseController.createCourse(Mockito.any(), Mockito.any())).thenThrow(
				new TransactionException(CourseEnum.CREATE_ERROR.getCode(), CourseEnum.CREATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			courseConnector.create(schoolId, courseApi);
		}).withMessage(CourseEnum.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenAddStudentIsOk() {
		boolean hasError = false;
		try {
			courseConnector.addStudent(schoolId, idCourse, idStudent);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}
	
	@Test
	public void whenAddStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),StudentEnum.UPDATE_ERROR.getDescription()))
				.when(courseController).addStudent(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());;
		String otherId="";
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			courseConnector.addStudent(otherId, idCourse, idStudent);;
		}).withMessage(StudentEnum.UPDATE_ERROR.getDescription());

	}
	

	@Test
	public void whenDeleteStudentIsOk() {
		boolean hasError = false;
		try {
			courseConnector.deleteStudent(schoolId, idCourse, idStudent);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}
	
	@Test
	public void whenDeleteStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),StudentEnum.UPDATE_ERROR.getDescription()))
				.when(courseController).deleteStudent(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());;
		String otherId="";
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			courseConnector.deleteStudent(otherId, idCourse, idStudent);;
		}).withMessage(StudentEnum.UPDATE_ERROR.getDescription());

	}
}
