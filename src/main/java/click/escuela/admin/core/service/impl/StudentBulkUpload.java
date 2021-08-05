package click.escuela.admin.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.Sheet;

import click.escuela.admin.core.enumator.EducationLevels;
import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.StudentApiFile;
import click.escuela.admin.core.provider.student.dto.FileError;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
import click.escuela.admin.core.service.BulkUpload;

@Service
public class StudentBulkUpload implements BulkUpload<StudentApiFile>{

	@Autowired
	private StudentServiceImpl studentService;
	
	private File file;
	
	@Override
	public List<StudentApiFile> readFile(File file) throws Exception {
		
		InputStream inputStream = null;
		Workbook wb = null;
		try {
			this.file = file;
			inputStream = new FileInputStream(file);
			wb = WorkbookFactory.create(inputStream);
			//TODO ver como saltear primer row
			Sheet sheet = wb.getSheetAt(0);
			List<StudentApiFile> students = new ArrayList<>();
			sheet.rowIterator().forEachRemaining(row -> students.add(buildStudentApi(row)));
			return students;
			
		}catch(EncryptedDocumentException|IOException ex) {
			throw ex;
			
		}
		
	}

	private StudentApiFile buildStudentApi(Row row) {
		
		String name = row.getCell(0).getStringCellValue();
		String surname = row.getCell(1).getStringCellValue();
		//String document = row.getCell(2).getStringCellValue();
		String gender = row.getCell(3).getStringCellValue();
		if(gender.equals("Masculino")) {
			gender = GenderType.MALE.toString();
		}
		else {
			gender = GenderType.FEMALE.toString();
		}
		String birthday = row.getCell(4).getStringCellValue();
		//String cellPhone = row.getCell(5).getStringCellValue();
		String email = row.getCell(6).getStringCellValue();
		//String grade = row.getCell(7).getStringCellValue();
		String division = row.getCell(8).getStringCellValue();
		String level = row.getCell(9).getStringCellValue();
		if(level.equals("Preescolar")) {
			level = EducationLevels.PREESCOLAR.toString();
		} else if (level.equals("Primario")) {
			level = EducationLevels.PRIMARIO.toString();
		}
		/*String parentName = row.getCell(13).getStringCellValue();
		String parentSurname = row.getCell(14).getStringCellValue();

		ParentApi parentApi =  ParentApi.builder().name(parentName).surname(parentSurname).build();
		*/
		StudentApiFile student = StudentApiFile.builder()
				.name(name)
				.surname(surname)
				//.document(document)
				.gender(gender)
				.birthday(LocalDate.parse(birthday))
				//.cellPhone(cellPhone)
				.division(division)
				//.grade(grade)
				.level(level)
				.email(email)
				.division(division)
				.build();
				/*.line(row.getRowNum())
				.parentApi(parentApi)*/
		return student;
				
	}

	@Override
	public List<FileError> upload(String schoolId, List<StudentApiFile> students) {
		List<FileError> errors = new ArrayList<>();
		students.stream().forEach(student -> {
			try {
				studentService.create(schoolId,student);
			} catch (TransactionException e) {
				//TODO crear un utils para extraer el error
				FileError fileError = FileError.builder()
				.errors(null)
				.line(student.getLine())
				.build();
				
				errors.add(fileError);
			}
		});
		
		return errors;
	}

	@Override
	public File writeErrors(List<FileError> errors) {
		// TODO Auto-generated method stub
		return this.file;
	}


}
