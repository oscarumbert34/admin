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
import click.escuela.admin.core.provider.student.connector.StudentConnector;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
import click.escuela.student.api.AdressApi;
import click.escuela.student.api.ParentApi;
import click.escuela.student.api.StudentApi;
import click.escuela.student.api.StudentUpdateApi;
import click.escuela.student.enumerator.GenderType;
import click.escuela.student.enumerator.StudentEnum;
import click.escuela.student.exception.TransactionException;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

	@Mock
	private StudentConnector studentConnector;
	
	private StudentServiceImpl studentServiceImpl = new StudentServiceImpl();
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
		doThrow( new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
				StudentEnum.UPDATE_ERROR.getDescription())).when(studentConnector).update(Mockito.any());
		
		studentUpdateApi.setId("7fsd7fsaf809s8fs8f9sd");
		assertThatExceptionOfType(TransactionException.class)
		  .isThrownBy(() -> {

				studentServiceImpl.update(studentUpdateApi);
		}).withMessage(StudentEnum.UPDATE_ERROR.getDescription());

	}
	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow( new TransactionException(StudentEnum.CREATE_ERROR.getCode(),
				StudentEnum.CREATE_ERROR.getDescription())).when(studentConnector).create(Mockito.any());
		
		StudentApi studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now())
				.cellPhone("4534543").document("55555").division("F").grade("3°").email("oscar@gmail.com").gender(GenderType.MALE.toString())
				.name("oscar").parentApi(new ParentApi()).school("1234").build();
		
		
		assertThatExceptionOfType(TransactionException.class)
		  .isThrownBy(() -> {

				studentServiceImpl.create(studentApi);
		}).withMessage(StudentEnum.CREATE_ERROR.getDescription());

	}
}
