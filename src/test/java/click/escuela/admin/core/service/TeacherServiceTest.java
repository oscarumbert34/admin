package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.TeacherConnector;
import click.escuela.admin.core.enumator.TeacherMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.service.impl.TeacherServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TeacherServiceTest {

	@Mock
	private TeacherConnector teacherConnector;

	private TeacherServiceImpl teacherServiceImpl = new TeacherServiceImpl();
	private TeacherApi teacherApi;
	private String schoolId;

	@Before
	public void setUp() throws TransactionException {

		schoolId = "1234";

		teacherApi = TeacherApi.builder().name("Mariana").surname("Lopez").birthday(LocalDate.now()).documentType("DNI")
				.document("25897863").cellPhone("1589632485").email("mariAna@gmail.com").adressApi(new AdressApi())
				.build();

		doNothing().when(teacherConnector).create(Mockito.any(), Mockito.any());

		ReflectionTestUtils.setField(teacherServiceImpl, "teacherConnector", teacherConnector);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.create(schoolId, teacherApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(TeacherMessage.CREATE_ERROR.getCode(),
				TeacherMessage.CREATE_ERROR.getDescription())).when(teacherConnector).create(Mockito.any(),
						Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			teacherServiceImpl.create(schoolId, teacherApi);
		}).withMessage(TeacherMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdateIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.update(schoolId, teacherApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {
		doThrow(new TransactionException(TeacherMessage.UPDATE_ERROR.getCode(),
				TeacherMessage.UPDATE_ERROR.getDescription())).when(teacherConnector).update(Mockito.any(),
						Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			teacherServiceImpl.update(schoolId, teacherApi);
		}).withMessage(TeacherMessage.UPDATE_ERROR.getDescription());

	}
}
