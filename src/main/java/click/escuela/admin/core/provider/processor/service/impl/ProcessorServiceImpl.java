package click.escuela.admin.core.provider.student.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	public void save(String schoolId, MultipartFile excel) throws TransactionException, EncryptedDocumentException, IOException {
		
		//File file = new File(excel.getOriginalFilename());
		File file = multipartToFile(excel,"/excel");
		List<StudentApiFile> students = studentBulkUpload.readFile(file);
		List<FileError> errors = studentBulkUpload.upload(schoolId, students); 
		studentBulkUpload.writeErrors(errors, file);
		ExcelApi excelApi= ExcelApi.builder().name(file.getName()).schoolId(Integer.valueOf(schoolId)).file(file.getAbsolutePath()).studentCount(students.size()).build();
		excelConnector.save(schoolId, excelApi);
	}
	
	
	private File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
		String listingFolder = System.getProperty("java.io.tmpdir");

		File convFile = new File(listingFolder,fileName);
	    multipart.transferTo(convFile);
	    return convFile;
	}
	
}
