package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.SchoolConnector;
import click.escuela.admin.core.enumator.SchoolMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.SchoolApi;

@RunWith(MockitoJUnitRunner.class)
public class SchoolConnectorTest {

	@Mock
	private StudentController schoolController;

	private SchoolConnector schoolConnector = new SchoolConnector();
	private SchoolApi schoolApi;

	@Before
	public void setUp() throws TransactionException {

		schoolApi = SchoolApi.builder().name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418").countCourses(10).countStudent(20)
				.build();
		ReflectionTestUtils.setField(schoolConnector, "schoolController", schoolController);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		schoolConnector.create(schoolApi);
		verify(schoolController).createSchool(schoolApi);
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(SchoolMessage.CREATE_ERROR.getCode(),
				SchoolMessage.CREATE_ERROR.getDescription())).when(schoolController).createSchool(Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			schoolConnector.create(Mockito.any());
		}).withMessage(SchoolMessage.CREATE_ERROR.getDescription());
	}

}
