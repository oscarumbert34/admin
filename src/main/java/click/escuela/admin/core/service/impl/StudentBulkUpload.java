package click.escuela.admin.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.StudentApiFile;
import click.escuela.admin.core.provider.student.dto.FileError;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
import click.escuela.admin.core.service.BulkUpload;

@Service
public class StudentBulkUpload implements BulkUpload<StudentApiFile>{

	@Autowired
	private StudentServiceImpl studentService;
	
	@Override
	public List<StudentApiFile> readFile(File file) throws FileNotFoundException, IOException {
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = wb.getSheetAt(0);
		List<StudentApiFile> students = new ArrayList<>();
		
		sheet.rowIterator().forEachRemaining(row -> students.add(buildStudentApi(row)));
		return students;
	}

	private StudentApiFile buildStudentApi(Row row) {
		String document = row.getCell(0).getStringCellValue();
		String cellphone = row.getCell(1).getStringCellValue();
		
		return StudentApiFile.builder()
				.cellPhone(cellphone)
				.document(document)
				.line(row.getRowNum())
				.build();
	}

	@Override
	public List<FileError> upload(List<StudentApiFile> students) {
		List<FileError> errors = new ArrayList<>();
		students.stream().forEach(student -> {
			try {
				studentService.create(student);
			} catch (TransactionException e) {
				FileError fileError = FileError.builder()
				.errors(null)
				.line(student.getLine())
				.build();
				
				errors.add(fileError);
			}
		});
		
		return errors;
	}


}
