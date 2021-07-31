package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.ExcelConnector;
import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.provider.student.api.ExcelApi;
import click.escuela.admin.core.provider.student.api.StudentApiFile;
import click.escuela.admin.core.provider.student.dto.FileError;
import click.escuela.admin.core.service.impl.StudentBulkUpload;

@Service
public class ExcelServiceImpl {

	@Autowired
	private ExcelConnector excelConnector;

	@Autowired
	private StudentBulkUpload studentBulkUpload;
	
	@Async
	public void save(String schoolId, ExcelApi excelApi) throws Exception {
		
		List<StudentApiFile> students = studentBulkUpload.readFile(null);
		List<FileError> errors = studentBulkUpload.upload(schoolId, students);
		
		studentBulkUpload.writeErrors(errors);
		
		
		//excelConnector.save(schoolId, excelApi);
	}

}
