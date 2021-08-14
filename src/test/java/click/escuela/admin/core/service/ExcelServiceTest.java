package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
import click.escuela.admin.core.enumator.EducationLevels;
import click.escuela.admin.core.enumator.ExcelMessage;
import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ExcelApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApiFile;
import click.escuela.admin.core.provider.student.service.impl.ExcelServiceImpl;
import click.escuela.admin.core.service.impl.StudentBulkUpload;

@RunWith(MockitoJUnitRunner.class)
public class ExcelServiceTest {

	@Mock
	private ExcelConnector excelConnector;
	
	@Mock
	private StudentBulkUpload studentBulkUpload;

	private ExcelServiceImpl excelServiceImpl = new ExcelServiceImpl();
//	private MultipartFile multipartFile;
	private String path = "EstudiantesTest.xlsx";
	private ExcelApi excelApi;
	private String schoolId = "1234";
	private File file = new File(path);
	private List<StudentApiFile> students =  new ArrayList<>();

	@Before
	public void setUp() throws Exception  {
//		//Path path = Paths.get(file.getAbsolutePath());
//		String name ="Estudiantes";
//		String originalFileName = "Estudiantes.xlsx";
//		String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//		byte[] content = file.getAbsolutePath().getBytes();
//		schoolId = Integer.toString(1234);
//		excelApi = ExcelApi.builder().name("Archivo").file("Archivo.excel").studentCount(20)
//				.schoolId(Integer.valueOf(schoolId)).build();
//		multipartFile = new MockMultipartFile(name,
//                originalFileName, contentType, content);
		StudentApiFile student =StudentApiFile.builder().name("Tony").surname("Liendro").document("377758269").gender(GenderType.MALE.toString())
				.cellPhone("1523554622").division("3").grade("7").level(EducationLevels.TERCIARIO.toString())
				.email("tony@gmail.com").adressApi(null).parentApi(null).line(1)
				.build();
		students.add(student);		
		when(studentBulkUpload.readFile(file)).thenReturn(students);
		doNothing().when(excelConnector).save( Mockito.anyString(),Mockito.any());
		ReflectionTestUtils.setField(excelServiceImpl, "excelConnector", excelConnector);
		ReflectionTestUtils.setField(excelServiceImpl, "studentBulkUpload", studentBulkUpload);

	}

	@Test
	public void whenCreateIsOk() throws Exception {
		/*excelApi= ExcelApi.builder().name(file.getName()).schoolId(Integer.valueOf(schoolId)).file(file.getAbsolutePath()).studentCount(students.size()).build();
		excelServiceImpl.save(schoolId, path);
		verify(excelConnector).save(schoolId, excelApi);*/
		boolean hasError = false;
		try {
			excelServiceImpl.save(schoolId, path);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws ExcelException {
		doThrow(new ExcelException(ExcelMessage.CREATE_ERROR)).when(excelConnector).save(Mockito.any(), Mockito.any());
		assertThatExceptionOfType(ExcelException.class).isThrownBy(() -> {
			excelServiceImpl.save(schoolId, path);
		}).withMessage(ExcelMessage.CREATE_ERROR.getDescription()); 
	}
}
