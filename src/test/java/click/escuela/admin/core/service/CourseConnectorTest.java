package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.admin.core.api.CourseApi;
import click.escuela.admin.core.connector.CourseConnector;
import click.escuela.admin.core.enumator.CourseEnum;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;

@RunWith(MockitoJUnitRunner.class)
public class CourseConnectorTest {

	private CourseConnector courseConnector = new CourseConnector();

	@Mock
	private StudentController courseController;
	private CourseApi courseApi;
	private String schoolId;

	@Before
	public void setUp() throws TransactionException {

		schoolId = "1234";
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

}
