package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.CourseConnector;
import click.escuela.admin.core.enumator.CourseMessage;
import click.escuela.admin.core.enumator.StudentMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.CourseApi;
import click.escuela.admin.core.provider.student.service.impl.CourseServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceTest {

	@Mock
	private CourseConnector courseConnector;

	private CourseServiceImpl courseServiceImpl = new CourseServiceImpl();
	private CourseApi courseApi;
	private String schoolId;
	private String studentId;
	private String courseId;
	private String teacherId;
	private String EMPTY = "";

	@Before
	public void setUp() throws TransactionException {
		schoolId = UUID.randomUUID().toString();
		studentId = UUID.randomUUID().toString();
		courseId = UUID.randomUUID().toString();
		teacherId = UUID.randomUUID().toString();
		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		doNothing().when(courseConnector).create(Mockito.any(), Mockito.any());
		doNothing().when(courseConnector).addStudent(schoolId, courseId, studentId);
		doNothing().when(courseConnector).deleteStudent(schoolId, courseId, studentId);
		doNothing().when(courseConnector).addTeacher(schoolId, courseId, teacherId);
		doNothing().when(courseConnector).deleteTeacher(schoolId, courseId, teacherId);

		ReflectionTestUtils.setField(courseServiceImpl, "courseConnector", courseConnector);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		courseServiceImpl.create(schoolId, courseApi);
		verify(courseConnector).create(schoolId, courseApi);
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.CREATE_ERROR.getCode(),
				CourseMessage.CREATE_ERROR.getDescription())).when(courseConnector).create(Mockito.any(),
						Mockito.any());
		CourseApi courseApi = CourseApi.builder().year(10).division("C").countStudent(40).schoolId(85252).build();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseServiceImpl.create(schoolId, courseApi);
		}).withMessage(CourseMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenAddStudentIsOk() throws TransactionException {
		courseServiceImpl.addStudent(schoolId, courseId, studentId);
		verify(courseConnector).addStudent(schoolId, courseId, studentId);
	}

	@Test
	public void whenAddStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentMessage.UPDATE_ERROR.getCode(),
				StudentMessage.UPDATE_ERROR.getDescription())).when(courseConnector).addStudent(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseServiceImpl.addStudent(EMPTY, courseId, studentId);
		}).withMessage(StudentMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenDeleteStudentIsOk() throws TransactionException {
		courseServiceImpl.deleteStudent(schoolId, courseId, studentId);
		verify(courseConnector).deleteStudent(schoolId, courseId, studentId);
	}

	@Test
	public void whenDeleteStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentMessage.UPDATE_ERROR.getCode(),
				StudentMessage.UPDATE_ERROR.getDescription())).when(courseConnector).deleteStudent(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		String otherId = "";
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseServiceImpl.deleteStudent(otherId, courseId, studentId);
		}).withMessage(StudentMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenAddTeacherIsOk() throws TransactionException {
		courseServiceImpl.addTeacher(schoolId, courseId, teacherId);
		verify(courseConnector).addTeacher(schoolId, courseId, teacherId);
	}

	@Test
	public void whenDeleteTeacherIsOk() throws TransactionException {
		courseServiceImpl.deleteTeacher(schoolId, courseId, teacherId);
		verify(courseConnector).deleteTeacher(schoolId, courseId, teacherId);
	}

	@Test
	public void whenAddTeacherIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(courseConnector).addTeacher(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseServiceImpl.addTeacher(EMPTY, EMPTY, EMPTY);
		}).withMessage(CourseMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenDeleteTeacherIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(courseConnector).deleteTeacher(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseServiceImpl.deleteTeacher(EMPTY, EMPTY, EMPTY);
		}).withMessage(CourseMessage.UPDATE_ERROR.getDescription());
	}
}
