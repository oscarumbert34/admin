package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.enumator.StudentMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.connector.StudentConnector;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

	@Mock
	private StudentConnector studentConnector;

	private StudentServiceImpl studentServiceImpl = new StudentServiceImpl();
	private StudentApi studentApi;
	private UUID studentId;
	private UUID idCourse;
	private String schoolId;
	private Boolean fullDetail;

	@Before
	public void setUp() throws TransactionException {
		studentId = UUID.randomUUID();
		idCourse = UUID.randomUUID();
		schoolId = "1234";
		fullDetail = true;

		ParentApi parentApi = new ParentApi();
		parentApi.setAdressApi(new AdressApi());

		studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now()).cellPhone("4534543")
				.division("C").grade("3°").document("435345").email("oscar@gmail.com")
				.gender(GenderType.MALE.toString()).name("oscar").parentApi(parentApi).build();

		doNothing().when(studentConnector).create(Mockito.anyString(), Mockito.any());

		ReflectionTestUtils.setField(studentServiceImpl, "studentConnector", studentConnector);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		studentServiceImpl.create(schoolId, studentApi);
		verify(studentConnector).create(schoolId, studentApi);
	}

	@Test
	public void whenUpdateOk() throws TransactionException {
		studentServiceImpl.update(schoolId, studentApi);
		verify(studentConnector).update(schoolId, studentApi);
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {
		doThrow(new TransactionException(StudentMessage.UPDATE_ERROR.getCode(),
				StudentMessage.UPDATE_ERROR.getDescription())).when(studentConnector).update(Mockito.anyString(),
						Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.update(schoolId, studentApi);
		}).withMessage(StudentMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(StudentMessage.CREATE_ERROR.getCode(),
				StudentMessage.CREATE_ERROR.getDescription())).when(studentConnector).create(Mockito.anyString(),
						Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.create(schoolId, studentApi);
		}).withMessage(StudentMessage.CREATE_ERROR.getDescription());

	}

	@Test
	public void whenGetByIdIsOk() throws TransactionException {
		studentServiceImpl.getById(schoolId.toString(), studentId.toString(), fullDetail);
		verify(studentConnector).getById(schoolId, studentId.toString(), fullDetail);
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		studentId = UUID.randomUUID();
		Mockito.when(studentConnector.getById(schoolId.toString(), studentId.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.getById(schoolId, studentId.toString(), fullDetail);
		}).withMessage(null);
	}

	@Test
	public void whengetBySchoolIsOk() throws TransactionException {
		studentServiceImpl.getBySchool(schoolId, fullDetail);
		verify(studentConnector).getBySchool(schoolId, fullDetail);
	}

	@Test
	public void whenGetgetBySchoolIsError() throws TransactionException {
		Mockito.when(studentConnector.getBySchool(schoolId.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.getBySchool(schoolId.toString(), fullDetail);
		}).withMessage(null);
	}

	@Test
	public void whenGetByIdCourseIsOK() throws TransactionException {
		studentServiceImpl.getByCourse(schoolId, idCourse.toString(), fullDetail);
		verify(studentConnector).getByCourse(schoolId, idCourse.toString(), fullDetail);
	}

	@Test
	public void whenGetByIdCourseIsError() throws TransactionException {
		idCourse = UUID.randomUUID();
		Mockito.when(studentConnector.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail);
		}).withMessage(null);
	}
}
