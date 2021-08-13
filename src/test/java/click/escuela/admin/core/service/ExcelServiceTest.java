package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;

import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.ExcelConnector;
import click.escuela.admin.core.enumator.ExcelMessage;
import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.provider.student.api.ExcelApi;
import click.escuela.admin.core.provider.student.service.impl.ExcelServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ExcelServiceTest {

	@Mock
	private ExcelConnector excelConnector;

	private ExcelServiceImpl excelServiceImpl = new ExcelServiceImpl();
	private MultipartFile multipartFile;
	private ExcelApi excelApi;
	private String schoolId;

	@Before
	public void setUp() throws ExcelException, IOException  {
		File file = new File("Estudiantes.xlsx");
		//Path path = Paths.get(file.getAbsolutePath());
		String name ="Estudiantes";
		String originalFileName = "Estudiantes.xlsx";
		String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		byte[] content = file.getAbsolutePath().getBytes();
		schoolId = Integer.toString(1234);
		excelApi = ExcelApi.builder().name("Archivo").file("Archivo.excel").studentCount(20)
				.schoolId(Integer.valueOf(schoolId)).build();
		multipartFile = new MockMultipartFile(name,
                originalFileName, contentType, content);
		
		doNothing().when(excelConnector).save(Mockito.any(), Mockito.any());
		ReflectionTestUtils.setField(excelServiceImpl, "excelConnector", excelConnector);
	}

	@Test
	public void whenCreateIsOk() throws Exception {
		excelServiceImpl.save(schoolId, multipartFile);
		verify(excelConnector).save(schoolId, excelApi);
	}

	@Test
	public void whenCreateIsError() throws ExcelException {
		doThrow(new ExcelException(ExcelMessage.CREATE_ERROR)).when(excelConnector).save(Mockito.any(), Mockito.any());
		assertThatExceptionOfType(ExcelException.class).isThrownBy(() -> {
			excelServiceImpl.save(schoolId, multipartFile);
		}).withMessage(ExcelMessage.CREATE_ERROR.getDescription());
	}
}
