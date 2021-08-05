package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.provider.student.api.StudentApiFile;
import click.escuela.admin.core.service.impl.StudentBulkUpload;

@RunWith(MockitoJUnitRunner.class)
public class StudentBulkUploadTest {

	@InjectMocks
	private StudentBulkUpload studentBulkUpload;
	
	
	@Test
	public void whenReadFileIsOk() throws Exception {
		
		File file = new File("C:\\Users\\liend\\Desktop\\estudiantes.xlsx");
		
		List<StudentApiFile> list= studentBulkUpload.readFile(file);
		assertThat(list).isNotEmpty();
	}
}
