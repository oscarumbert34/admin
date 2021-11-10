package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.SchoolConnector;
import click.escuela.admin.core.enumator.EmailMessage;
import click.escuela.admin.core.enumator.SchoolMessage;
import click.escuela.admin.core.exception.EmailException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.ProcessorController;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.SchoolApi;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorConnectorTest {

	@Mock
	private ProcessorController processorController;

	private ProcessorConnector processorConnector = new ProcessorConnector();
	private String email;
	private String userName;
	private String password;
	private String schoolId;

	@Before
	public void setUp() throws TransactionException {
		
		email = "colegionacional@edu.gob.com";
		userName = "Tony2022";
		password = "Tony2022";
		schoolId = UUID.randomUUID().toString();

		ReflectionTestUtils.setField(processorConnector, "processorController", processorController);
	}

	@Test
	public void whenSendEmailOk() throws EmailException {
		processorConnector.sendEmail(password, userName, email, schoolId);
		verify(processorController).sendEmail(password, userName, email, schoolId);
	}

	@Test
	public void whenCreateIsError() throws EmailException {
		doThrow(new EmailException(EmailMessage.SEND_ERROR)).when(processorController).sendEmail(password, userName, email, schoolId);
		assertThatExceptionOfType(EmailException.class).isThrownBy(() -> {
			processorConnector.sendEmail(password, userName, email, schoolId);
		}).withMessage(EmailMessage.SEND_ERROR.getDescription());
	}

}
