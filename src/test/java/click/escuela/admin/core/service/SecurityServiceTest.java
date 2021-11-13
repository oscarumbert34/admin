package click.escuela.admin.core.service;

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
import click.escuela.admin.core.provider.processor.service.impl.SecurityServiceImpl;
import click.escuela.admin.core.provider.student.api.UserApi;
import click.escuela.admin.core.provider.student.connector.SecurityConnector;

@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceTest {
	
	@Mock
	private SecurityConnector securityConnector;

	private SecurityServiceImpl securityService = new SecurityServiceImpl();
	private UserApi userApi;

	@Before
	public void setUp() throws TransactionException {

		userApi = UserApi.builder().userId(UUID.randomUUID().toString()).userName("OscarGomez").email("oscar@gmail.com")
				.name("Oscar").password("Oscar2020").surname("gomez").schoolId(UUID.randomUUID().toString())
				.role("STUDENT").build();

		ReflectionTestUtils.setField(securityService, "securityConnector", securityConnector);
	}

	@Test
	public void whenSaveUserOk() throws SchoolException {
		securityService.saveUser(userApi);
		verify(securityConnector).saveUser(userApi);
	}

	@Test
	public void whenSaveUserError() throws SchoolException {
		doThrow(new SchoolException(SchoolMessage.GET_ERROR)).when(securityConnector).saveUser(userApi);
		assertThatExceptionOfType(SchoolException.class).isThrownBy(() -> {
			securityService.saveUser(userApi);
		}).withMessage(SchoolMessage.GET_ERROR.getDescription());
	}

}
