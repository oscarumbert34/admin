package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.ExcelConnector;
import click.escuela.admin.core.enumator.ExcelMessage;
import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.ExcelApi;
import click.escuela.admin.core.provider.student.service.impl.ExcelServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ExcelServiceTest {

	@Mock
	private ExcelConnector excelConnector;

	private ExcelServiceImpl excelServiceImpl = new ExcelServiceImpl();
	private ExcelApi excelApi;
	private String schoolId;

	@Before
	public void setUp() throws ExcelException  {
		schoolId = Integer.toString(1234);
		excelApi = ExcelApi.builder().name("Archivo").file("Archivo.excel").studentCount(20)
				.schoolId(Integer.valueOf(schoolId)).build();
		
		doNothing().when(excelConnector).save(Mockito.any(), Mockito.any());
		ReflectionTestUtils.setField(excelServiceImpl, "excelConnector", excelConnector);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		excelServiceImpl.save(schoolId, excelApi);
		verify(excelConnector).save(schoolId, excelApi);
	}

	@Test
	public void whenCreateIsError() throws ExcelException {
		doThrow(new ExcelException(ExcelMessage.CREATE_ERROR)).when(excelConnector).save(Mockito.any(), Mockito.any());
		assertThatExceptionOfType(ExcelException.class).isThrownBy(() -> {
			excelServiceImpl.save(schoolId, excelApi);
		}).withMessage(ExcelMessage.CREATE_ERROR.getDescription());
	}
}
