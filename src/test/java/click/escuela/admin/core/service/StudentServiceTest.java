package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

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
import click.escuela.admin.core.enumator.StudentEnum;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.StudentUpdateApi;
import click.escuela.admin.core.provider.student.connector.StudentConnector;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

	@Mock
	private StudentConnector studentConnector;

	private StudentServiceImpl studentServiceImpl = new StudentServiceImpl();
	private StudentApi studentApi;
	private StudentUpdateApi studentUpdateApi;
	private UUID studentId;
	private UUID idCourse;
	private Integer schoolId;
	private Boolean fullDetail;

	@Before
	public void setUp() throws TransactionException {
		studentId = UUID.randomUUID();
		idCourse = UUID.randomUUID();
		schoolId = 1234;
		fullDetail = true;

		ParentApi parentApi = new ParentApi();
		parentApi.setAdressApi(new AdressApi());

		studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now()).cellPhone("4534543")
				.division("C").grade("3°").document("435345").email("oscar@gmail.com")
				.gender(GenderType.MALE.toString()).name("oscar").parentApi(parentApi).schoolId(1234).build();

		studentUpdateApi = new StudentUpdateApi(studentApi);
		studentUpdateApi.setId(studentId.toString());
		doNothing().when(studentConnector).create(Mockito.any());

		ReflectionTestUtils.setField(studentServiceImpl, "studentConnector", studentConnector);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			studentServiceImpl.create(studentApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateOk() {

		boolean hasError = false;
		try {
			studentServiceImpl.update(studentUpdateApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {
		doThrow(new TransactionException(StudentEnum.UPDATE_ERROR.getCode(), StudentEnum.UPDATE_ERROR.getDescription()))
				.when(studentConnector).update(Mockito.any());

		studentUpdateApi.setId("7fsd7fsaf809s8fs8f9sd");
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			studentServiceImpl.update(studentUpdateApi);
		}).withMessage(StudentEnum.UPDATE_ERROR.getDescription());

	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(StudentEnum.CREATE_ERROR.getCode(), StudentEnum.CREATE_ERROR.getDescription()))
				.when(studentConnector).create(Mockito.any());

		StudentApi studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now())
				.cellPhone("4534543").document("55555").division("F").grade("3°").email("oscar@gmail.com")
				.gender(GenderType.MALE.toString()).name("oscar").parentApi(new ParentApi()).schoolId(1234).build();

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			studentServiceImpl.create(studentApi);
		}).withMessage(StudentEnum.CREATE_ERROR.getDescription());

	}

	@Test
	public void whenGetByIdIsOk() {
		boolean hasError = false;
		try {
			studentServiceImpl.getById(schoolId.toString(), studentId.toString(), fullDetail);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		schoolId = 1234;
		studentId = UUID.randomUUID();
		Mockito.when(studentConnector.getById(schoolId.toString(), studentId.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.getById(schoolId.toString(), studentId.toString(), fullDetail);
		}).withMessage(null);
	}

	@Test
	public void whengetBySchoolIsOk() {
		boolean hasError = false;
		try {
			studentServiceImpl.getBySchool(schoolId.toString(), fullDetail);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetgetBySchoolIsError() throws TransactionException {
		schoolId = 2143;
		Mockito.when(studentConnector.getBySchool(schoolId.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.getBySchool(schoolId.toString(), fullDetail);
		}).withMessage(null);
	}

	@Test
	public void whenGetByIdCourseIsOK() throws TransactionException {
		boolean hasError = false;
		try {
			studentServiceImpl.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdCourseIsError() throws TransactionException {
		schoolId = 2143;
		idCourse = UUID.randomUUID();
		Mockito.when(studentConnector.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail);
		}).withMessage(null);
	}
}
