package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.CourseConnector;
import click.escuela.admin.core.enumator.CourseEnum;
import click.escuela.admin.core.enumator.StudentEnum;
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
	private String idStudent;
	private String idCourse;

	@Before
	public void setUp() throws TransactionException {

		schoolId = UUID.randomUUID().toString();
		idStudent = UUID.randomUUID().toString();
		idCourse = UUID.randomUUID().toString();

		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		doNothing().when(courseConnector).create(Mockito.any(), Mockito.any());
		doNothing().when(courseConnector).addStudent(schoolId, idCourse, idStudent);
		doNothing().when(courseConnector).deleteStudent(schoolId, idCourse, idStudent);

		ReflectionTestUtils.setField(courseServiceImpl, "courseConnector", courseConnector);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			courseServiceImpl.create(schoolId, courseApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(CourseEnum.CREATE_ERROR.getCode(), CourseEnum.CREATE_ERROR.getDescription()))
				.when(courseConnector).create(Mockito.any(), Mockito.any());

		CourseApi courseApi = CourseApi.builder().year(10).division("C").countStudent(40).schoolId(85252).build();

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			courseServiceImpl.create(schoolId, courseApi);
		}).withMessage(CourseEnum.CREATE_ERROR.getDescription());

	}

	@Test
	public void whenAddStudentIsOk() {
		boolean hasError = false;
		try {
			courseServiceImpl.addStudent(schoolId, idCourse, idStudent);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenAddStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentEnum.UPDATE_ERROR.getCode(), StudentEnum.UPDATE_ERROR.getDescription()))
				.when(courseConnector).addStudent(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		;
		String otherId = "";
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			courseServiceImpl.addStudent(otherId, idCourse, idStudent);
			;
		}).withMessage(StudentEnum.UPDATE_ERROR.getDescription());

	}

	@Test
	public void whenDeleteStudentIsOk() {
		boolean hasError = false;
		try {
			courseServiceImpl.deleteStudent(schoolId, idCourse, idStudent);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenDeleteStudentIsError() throws TransactionException {
		doThrow(new TransactionException(StudentEnum.UPDATE_ERROR.getCode(), StudentEnum.UPDATE_ERROR.getDescription()))
				.when(courseConnector).deleteStudent(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		;
		String otherId = "";
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			courseServiceImpl.deleteStudent(otherId, idCourse, idStudent);
			;
		}).withMessage(StudentEnum.UPDATE_ERROR.getDescription());

	}

}
