package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.admin.core.connector.CourseConnector;
import click.escuela.admin.core.enumator.CourseMessage;
import click.escuela.admin.core.enumator.StudentMessage;
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
	private String studentId;
	private String courseId;
	private String teacherId;
	
	@Before
	public void setUp() throws TransactionException {
		schoolId = UUID.randomUUID().toString();
		studentId = UUID.randomUUID().toString();
		courseId = UUID.randomUUID().toString();
		teacherId = UUID.randomUUID().toString();
		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		ReflectionTestUtils.setField(courseConnector, "courseController", courseController);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		courseConnector.create(schoolId, courseApi);
		verify(courseController).createCourse(schoolId, courseApi);
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		when(courseController.createCourse(Mockito.any(), Mockito.any())).thenThrow(new TransactionException(
				CourseMessage.CREATE_ERROR.getCode(), CourseMessage.CREATE_ERROR.getDescription()));
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseConnector.create(schoolId, courseApi);
		}).withMessage(CourseMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenAddStudentIsOk() throws TransactionException {
		courseConnector.addStudent(schoolId, courseId, studentId);
		verify(courseController).addStudent(schoolId, courseId, studentId);
	}

	@Test
	public void whenAddStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentMessage.UPDATE_ERROR.getCode(),
				StudentMessage.UPDATE_ERROR.getDescription())).when(courseController).addStudent(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseConnector.addStudent(StringUtils.EMPTY, courseId, studentId);
		}).withMessage(StudentMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenDeleteStudentIsOk() throws TransactionException {
		courseConnector.deleteStudent(schoolId, courseId, studentId);
		verify(courseController).deleteStudent(schoolId, courseId, studentId);
	}

	@Test
	public void whenDeleteStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentMessage.UPDATE_ERROR.getCode(),
				StudentMessage.UPDATE_ERROR.getDescription())).when(courseController).deleteStudent(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseConnector.deleteStudent(StringUtils.EMPTY, courseId, studentId);
		}).withMessage(StudentMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenAddTeacherIsOk() throws TransactionException {
		courseConnector.addTeacher(schoolId, courseId, teacherId);
		verify(courseController).addTeacher(schoolId, courseId, teacherId);
	}

	@Test
	public void whenDeleteTeacherIsOk() throws TransactionException {
		courseConnector.deleteTeacher(schoolId, courseId, teacherId);
		verify(courseController).deleteTeacher(schoolId, courseId, teacherId);
	}

	@Test
	public void whenAddTeacherIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(courseController).addTeacher(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseConnector.addTeacher(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
		}).withMessage(CourseMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenDeleteTeacherIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(courseController).deleteTeacher(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseConnector.deleteTeacher(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
		}).withMessage(CourseMessage.UPDATE_ERROR.getDescription());
	}

}
