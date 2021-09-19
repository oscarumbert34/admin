package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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

	@Before
	public void setUp() throws TransactionException {
		schoolId = UUID.randomUUID().toString();
		studentId = UUID.randomUUID().toString();
		courseId = UUID.randomUUID().toString();
		courseApi = CourseApi.builder().year(8).division("B").build();

		doNothing().when(courseConnector).create(Mockito.any(), Mockito.any());
		doNothing().when(courseConnector).addStudent(schoolId, courseId, studentId);
		doNothing().when(courseConnector).deleteStudent(schoolId, courseId, studentId);

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
		CourseApi courseApi = CourseApi.builder().year(10).division("C").build();
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
			courseServiceImpl.addStudent(StringUtils.EMPTY, courseId, studentId);
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

}
