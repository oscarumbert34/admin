package click.escuela.admin.core.provider.student.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.ExcelConnector;
import click.escuela.admin.core.exception.TransactionException;
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
	public void save(String schoolId, String path) throws TransactionException {
		File file = new File(path);
		List<StudentApiFile> students = new ArrayList<>();
		try {
			students = studentBulkUpload.readFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<FileError> errors = studentBulkUpload.upload(schoolId, students); 
		studentBulkUpload.writeErrors(errors, file);
		ExcelApi excelApi= ExcelApi.builder().name(file.getName()).schoolId(Integer.valueOf(schoolId)).file(file.getAbsolutePath()).studentCount(students.size()).build();

		excelConnector.save(schoolId, excelApi);
	}
	
}
