package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.enumator.SchoolMessage;
import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.SecurityController;
import click.escuela.admin.core.provider.student.api.UserApi;
import click.escuela.admin.core.provider.student.connector.SecurityConnector;

@RunWith(MockitoJUnitRunner.class)
public class SecurityConnectorTest {

	@Mock
	private SecurityController securityController;

	private SecurityConnector securityConnector = new SecurityConnector();
	private UserApi userApi;

	@Before
	public void setUp() throws TransactionException {

		userApi = UserApi.builder().userId(UUID.randomUUID().toString()).userName("OscarGomez").email("oscar@gmail.com")
				.name("Oscar").password("Oscar2020").surname("gomez").schoolId(UUID.randomUUID().toString())
				.role("STUDENT").build();

		ReflectionTestUtils.setField(securityConnector, "securityController", securityController);
	}

	@Test
	public void whenSaveUserOk() throws SchoolException {
		securityConnector.saveUser(userApi);
		verify(securityController).saveUser(userApi);
	}

	@Test
	public void whenSaveUserError() throws SchoolException {
		doThrow(new SchoolException(SchoolMessage.GET_ERROR)).when(securityController).saveUser(userApi);
		assertThatExceptionOfType(SchoolException.class).isThrownBy(() -> {
			securityConnector.saveUser(userApi);
		}).withMessage(SchoolMessage.GET_ERROR.getDescription());
	}

}
