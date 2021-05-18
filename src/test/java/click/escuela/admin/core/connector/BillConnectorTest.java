package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.admin.core.connector.BillConnector;
import click.escuela.admin.core.enumator.BillMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.BillApi;

@RunWith(MockitoJUnitRunner.class)
public class BillConnectorTest {

	@Mock
	private StudentController billController;

	private BillConnector billConnector = new BillConnector();
	private BillApi billApi;
	private String schoolId;
	private String studentId;

	@Before
	public void setUp() throws TransactionException {

		schoolId = UUID.randomUUID().toString();
		studentId = UUID.randomUUID().toString();

		billApi = BillApi.builder().period(2021).file("Mayo").amount((double) 12000).build();

		ReflectionTestUtils.setField(billConnector, "billController", billController);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			billConnector.create(schoolId, studentId, billApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws TransactionException {

		when(billController.createBill(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(
				new TransactionException(BillMessage.CREATE_ERROR.getCode(), BillMessage.CREATE_ERROR.getDescription()));

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			billConnector.create(schoolId, studentId, billApi);
		}).withMessage(BillMessage.CREATE_ERROR.getDescription());
	}
}
