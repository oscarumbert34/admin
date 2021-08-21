package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.ExcelConnector;
import click.escuela.admin.core.enumator.EducationLevels;
import click.escuela.admin.core.enumator.ExcelMessage;
import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.exception.ExcelException;
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
//	private ExcelApi excelApi;
	private String schoolId = "1234";
	private MockMultipartFile multipart ;
	private File file = new File("EstudiantesTest.xlsx");
	private List<StudentApiFile> students =  new ArrayList<>();

	@Before
	public void setUp() throws Exception  {
		InputStream fileInp = new FileInputStream(file);
		multipart = new MockMultipartFile("EstudiantesTest.xlsx", file.getName(), "application/vnd.ms-excel",fileInp);
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
			excelServiceImpl.save(schoolId, multipart);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() throws ExcelException {
		doThrow(new ExcelException(ExcelMessage.CREATE_ERROR)).when(excelConnector).save(Mockito.any(), Mockito.any());
		assertThatExceptionOfType(ExcelException.class).isThrownBy(() -> {
			excelServiceImpl.save(schoolId, multipart);
		}).withMessage(ExcelMessage.CREATE_ERROR.getDescription()); 
	}
}
