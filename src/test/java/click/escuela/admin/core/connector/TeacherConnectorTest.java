package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.admin.core.enumator.TeacherMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.TeacherApi;

@RunWith(MockitoJUnitRunner.class)
public class TeacherConnectorTest {

	@Mock
	private StudentController teacherController;

	private TeacherConnector teacherConnector = new TeacherConnector();
	private TeacherApi teacherApi;
	private String schoolId;

	@Before
	public void setUp() throws TransactionException {

		schoolId = "1234";

		teacherApi = TeacherApi.builder().name("Mariana").surname("Lopez").birthday(LocalDate.now()).documentType("DNI")
				.document("25897863").cellPhone("1589632485").email("mariAna@gmail.com")
				.adressApi(new AdressApi()).build();

		ReflectionTestUtils.setField(teacherConnector, "teacherController", teacherController);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			teacherConnector.create(schoolId, teacherApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws TransactionException {

		when(teacherController.createTeacher(Mockito.any(), Mockito.any())).thenThrow(new TransactionException(
				TeacherMessage.CREATE_ERROR.getCode(), TeacherMessage.CREATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			teacherConnector.create(schoolId, teacherApi);
		}).withMessage(TeacherMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenUpdateIsOk() {
		boolean hasError = false;
		try {
			teacherConnector.update(schoolId, teacherApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {

		when(teacherController.updateTeacher(Mockito.any(), Mockito.any())).thenThrow(new TransactionException(
				TeacherMessage.UPDATE_ERROR.getCode(), TeacherMessage.UPDATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			teacherConnector.update(schoolId, teacherApi);
		}).withMessage(TeacherMessage.UPDATE_ERROR.getDescription());
	}
}
