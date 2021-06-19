package click.escuela.admin.core.connector;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.admin.core.enumator.ExcelMessage;
import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.ExcelApi;

@RunWith(MockitoJUnitRunner.class)
public class ExcellConnectorTest {

	@Mock
	private StudentController excelController;

	private ExcelConnector excelConnector = new ExcelConnector();
	private ExcelApi excelApi;
	private String schoolId;

	@Before
	public void setUp() {
		schoolId = Integer.toString(1234);
		excelApi = ExcelApi.builder().name("Archivo").file("Archivo.excel").studentCount(20)
				.schoolId(Integer.valueOf(schoolId)).build();

		ReflectionTestUtils.setField(excelConnector, "excelController", excelController);
	}

	@Test
	public void whenCreateIsOk() throws ExcelException {
		excelConnector.save(schoolId, excelApi);
		verify(excelController).saveExcel(schoolId, excelApi);
	}

	@Test
	public void whenCreateIsError() throws ExcelException {
		when(excelController.saveExcel(Mockito.any(), Mockito.any()))
				.thenThrow(new ExcelException(ExcelMessage.CREATE_ERROR));
		assertThatExceptionOfType(ExcelException.class).isThrownBy(() -> {
			excelConnector.save(schoolId, excelApi);
		}).withMessage(ExcelMessage.CREATE_ERROR.getDescription());
	}
}
