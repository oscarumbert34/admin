package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.enumator.StudentMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.StudentUpdateApi;
import click.escuela.admin.core.provider.student.connector.StudentConnector;

@RunWith(MockitoJUnitRunner.class)
public class StudentConnectorTest {

	private StudentConnector studentConnector = new StudentConnector();

	@Mock
	private StudentController studentController;

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
		fullDetail = false;

		ParentApi parentApi = new ParentApi();
		parentApi.setAdressApi(new AdressApi());

		studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now()).cellPhone("4534543")
				.division("C").grade("3°").document("435345").email("oscar@gmail.com")
				.gender(GenderType.MALE.toString()).name("oscar").parentApi(parentApi).schoolId(1234).build();

		studentUpdateApi = new StudentUpdateApi(studentApi);
		studentUpdateApi.setId("5534r34rfwef433t434r");
		// when(studentController.create("", studentApi)).thenReturn("");

		ReflectionTestUtils.setField(studentConnector, "studentController", studentController);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			studentConnector.create(studentApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateOk() {

		boolean hasError = false;
		try {
			studentConnector.update(studentUpdateApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {
		when(studentController.updateStudent(Mockito.any(), Mockito.any())).thenThrow(new TransactionException(
				StudentMessage.UPDATE_ERROR.getCode(), StudentMessage.UPDATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			studentConnector.update(studentUpdateApi);
		}).withMessage(StudentMessage.UPDATE_ERROR.getDescription());

	}

	@Test
	public void whenCreateIsError() throws TransactionException {

		when(studentController.createStudent(Mockito.any(), Mockito.any())).thenThrow(new TransactionException(
				StudentMessage.CREATE_ERROR.getCode(), StudentMessage.CREATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			studentConnector.create(studentApi);
		}).withMessage(StudentMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenGetByIdIsOk() {
		boolean hasError = false;
		try {
			studentConnector.getById(schoolId.toString(), studentId.toString(), fullDetail);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		schoolId = 1234;
		studentId = UUID.randomUUID();
		Mockito.when(studentController.getById(schoolId.toString(), studentId.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentConnector.getById(schoolId.toString(), studentId.toString(), fullDetail);
		}).withMessage(null);
	}

	@Test
	public void whengetBySchoolIsOk() {
		boolean hasError = false;
		try {
			studentConnector.getBySchool(schoolId.toString(), fullDetail);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetgetBySchoolIsError() throws TransactionException {
		schoolId = 2143;
		Mockito.when(studentController.getBySchool(schoolId.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentConnector.getBySchool(schoolId.toString(), fullDetail);
		}).withMessage(null);
	}

	@Test
	public void whenGetByIdCourseIsOK() throws TransactionException {
		boolean hasError = false;
		try {
			studentConnector.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdCourseIsError() throws TransactionException {
		schoolId = 2143;
		idCourse = UUID.randomUUID();
		Mockito.when(studentController.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail))
				.thenThrow(TransactionException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentConnector.getByCourse(schoolId.toString(), idCourse.toString(), fullDetail);
		}).withMessage(null);
	}

}
