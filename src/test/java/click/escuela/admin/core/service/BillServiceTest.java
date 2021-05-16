package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.BillConnector;
import click.escuela.admin.core.enumator.CourseMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.BillApi;
import click.escuela.admin.core.provider.student.service.impl.BillServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class BillServiceTest {

	@Mock
	private BillConnector billConnector;

	private BillServiceImpl billServiceImpl = new BillServiceImpl();
	private BillApi billApi;
	private String schoolId;
	private String studentId;

	@Before
	public void setUp() throws TransactionException {

		schoolId = UUID.randomUUID().toString();
		studentId = UUID.randomUUID().toString();

		billApi = BillApi.builder().period(2021).file("Mayo").amount((double) 12000).build();

		doNothing().when(billConnector).create(Mockito.any(), Mockito.any(), Mockito.any());

		ReflectionTestUtils.setField(billServiceImpl, "billConnector", billConnector);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			billServiceImpl.create(schoolId, studentId, billApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.CREATE_ERROR.getCode(), CourseMessage.CREATE_ERROR.getDescription()))
				.when(billConnector).create(Mockito.any(), Mockito.any(), Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			billServiceImpl.create(schoolId, studentId, billApi);
		}).withMessage(CourseMessage.CREATE_ERROR.getDescription());

	}
}
