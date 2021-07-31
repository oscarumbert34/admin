package click.escuela.admin.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.service.impl.StudentBulkUpload;

@RunWith(MockitoJUnitRunner.class)
public class StudentBulkUploadTest {

	@InjectMocks
	private StudentBulkUpload studentBulkUpload;
	
	
	@Test
	public void whenReadFileIsOk() throws Exception {
		
		File file = new File("C:\\Users\\oscar\\Documents\\alumnos2.xlsx");
		
		studentBulkUpload.readFile(file);
	}
}
