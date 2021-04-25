package click.escuela.admin.core.service;

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

import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.enumator.StudentEnum;
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
	
	@Before
	public void setUp() throws TransactionException {
		
		ParentApi parentApi = new ParentApi();
		parentApi.setAdressApi(new AdressApi());
		
		studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now())
				.cellPhone("4534543").division("C").grade("3°").document("435345").email("oscar@gmail.com").gender(GenderType.MALE.toString())
				.name("oscar").parentApi(parentApi).school("1234").build();

		studentUpdateApi = new StudentUpdateApi(studentApi);
		studentUpdateApi.setId("5534r34rfwef433t434r");
		//when(studentController.create("", studentApi)).thenReturn("");

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
		when(studentController.update(Mockito.any(),Mockito.any())).thenThrow(new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
				StudentEnum.UPDATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class)
		  .isThrownBy(() -> {

			  studentConnector.update(studentUpdateApi);
		}).withMessage(StudentEnum.UPDATE_ERROR.getDescription());

	}
	@Test
	public void whenCreateIsError() throws TransactionException {
		
		when(studentController.create(Mockito.any(),Mockito.any())).thenThrow(new TransactionException(StudentEnum.CREATE_ERROR.getCode(),
				StudentEnum.CREATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class)
		  .isThrownBy(() -> {

			  studentConnector.create(studentApi);
		}).withMessage(StudentEnum.CREATE_ERROR.getDescription());
		
	}
	
}
