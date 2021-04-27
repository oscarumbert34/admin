package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.api.CourseApi;
import click.escuela.admin.core.connector.CourseConnector;
import click.escuela.admin.core.enumator.CourseEnum;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.service.impl.CourseServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ControllerServiceTest {

	@Mock
	private CourseConnector courseConnector;

	private CourseServiceImpl courseServiceImpl = new CourseServiceImpl();
	private CourseApi courseApi;
	private String schoolId;

	@Before
	public void setUp() throws TransactionException {

		schoolId = "1234";
		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		doNothing().when(courseConnector).create(Mockito.any(), Mockito.any());

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
}
