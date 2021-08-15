package click.escuela.admin.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Sheet;

import click.escuela.admin.core.enumator.EducationLevels;
import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApiFile;
import click.escuela.admin.core.provider.student.dto.FileError;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
import click.escuela.admin.core.service.BulkUpload;

@Service
public class StudentBulkUpload implements BulkUpload<StudentApiFile> {

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
			Sheet sheet = wb.getSheetAt(0);
			List<StudentApiFile> students = new ArrayList<>();
			Iterator<Row> sheetWithoutTitle = sheet.rowIterator();
			sheetWithoutTitle.next();
			while (sheetWithoutTitle.hasNext()) {
				Row row = sheetWithoutTitle.next();
				// cell = -1 si es null o cell = 0 si no es null
				short cell = row.getFirstCellNum();
				if (cell == 0) {
					students.add(buildStudentApi(row));
				} else {
					break;
				}
			}
			return students;

		} catch (EncryptedDocumentException | IOException ex) {
			throw ex;

		}

	}

	private StudentApiFile buildStudentApi(Row row) {

		String name = null;
		if(row.getCell(0) != null) {
			name =  row.getCell(0).getStringCellValue();
		}
		String surname = null;
		if(row.getCell(1) != null) {
			surname =  row.getCell(1).getStringCellValue();
		}
		String document = null;
		if(row.getCell(2) != null) {
			document =  row.getCell(2).getStringCellValue();
		}
		String gender = null;
		if(row.getCell(3) != null) {
			gender =  row.getCell(3).getStringCellValue();
			if (gender != null && gender.equals("Masculino")) {
				gender = GenderType.MALE.toString();
			} else {
				gender = GenderType.FEMALE.toString();
			}
		}
		String birthday = null ;
		if(row.getCell(4) != null) {
			birthday =   row.getCell(4).getStringCellValue();
		}
		String cellPhone = null;
		if(row.getCell(5) != null) {
			cellPhone =   row.getCell(5).getStringCellValue();
		}
		String email =null;
		if( row.getCell(6) != null) {
			email =  row.getCell(6).getStringCellValue();
		}
		String grade =null;
		if( row.getCell(7) != null) {
			grade =  row.getCell(7).getStringCellValue();
		}
		String division =null;
		if( row.getCell(8) != null) {
			division =  row.getCell(8).getStringCellValue();
		}
		String level = null;
		if(row.getCell(9) != null) {
			level =  row.getCell(9).getStringCellValue();
			if (level.equals("Preescolar")) {
				level = EducationLevels.PREESCOLAR.toString();
			} else if (level.equals("Primario")) {
				level = EducationLevels.PRIMARIO.toString();
			}else if (level.equals("Secundario")) {
				level = EducationLevels.SECUNDARIO.toString();
			}else if( level.equals("Terciario")){
				level = EducationLevels.TERCIARIO.toString();
			}
		}
		String street = null;
		if( row.getCell(10) != null) {
			street =  row.getCell(10).getStringCellValue();
		}
		String number = null;
		if( row.getCell(11) != null) {
			number =  row.getCell(11).getStringCellValue();
		}
		String locality = null;
		if( row.getCell(12) != null) {
			locality =  row.getCell(12).getStringCellValue();
		}
		String parentName =null;
		if( row.getCell(13) != null) {
			parentName =  row.getCell(13).getStringCellValue();
		}
		String parentSurname =null;
		if( row.getCell(14) != null) {
			parentSurname =  row.getCell(14).getStringCellValue();
		}
		String parentDocument =null;
		if( row.getCell(15) != null) {
			parentDocument =  row.getCell(15).getStringCellValue();
		}
		String parentGender =  null;
		if(row.getCell(16) != null) {
			parentGender =  row.getCell(16).getStringCellValue();
			if (gender.equals("Masculino")) {
				parentGender = GenderType.MALE.toString();
			} else {
				parentGender = GenderType.FEMALE.toString();
			}
		}
		String parentBirthday = null;
		if( row.getCell(17) != null) {
			parentBirthday =  row.getCell(17).getStringCellValue();
		}
		String parentCellPhone =  null;
		if( row.getCell(18) != null) {
			parentCellPhone =  row.getCell(18).getStringCellValue();
		}
		String parentEmail =  null;
		if( row.getCell(19) != null) {
			parentEmail =  row.getCell(19).getStringCellValue();
		}

		AdressApi adressApi = AdressApi.builder().street(street).number(number).locality(locality).build();
		ParentApi parentApi = ParentApi.builder().name(parentName).surname(parentSurname).document(parentDocument)
				.gender(parentGender).cellPhone(parentCellPhone)
				.email(parentEmail).adressApi(adressApi).build();
		if(parentBirthday != null) {
			parentApi.setBirthday(LocalDate.parse(parentBirthday));
		}
		StudentApiFile student = StudentApiFile.builder().name(name).surname(surname).document(document).gender(gender)
				.cellPhone(cellPhone).division(division).grade(grade).level(level)
				.email(email).adressApi(adressApi).parentApi(parentApi).line(row.getRowNum())
				.build();
		if(birthday != null) {
			student.setBirthday(LocalDate.parse(birthday));
		}
		return student;

	}

	@Override
	public List<FileError> upload(String schoolId, List<StudentApiFile> students) {
		List<FileError> errors = new ArrayList<>();
		students.stream().forEach(student -> {
			try {
				studentService.create(schoolId, student);
			} catch (Exception e) {
				List<String> errorsList = new ArrayList<>();
				String error = e.getMessage();
				errorsList.add(error);
				FileError fileError = FileError.builder().errors(errorsList).line(student.getLine()).build();

				errors.add(fileError);
			}
		});

		return errors;
	}

	@Override
	public File writeErrors(List<FileError> errors, File file) {
		InputStream inputStream = null;
		Workbook wb = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb = WorkbookFactory.create(inputStream);
			Sheet sheet = wb.getSheetAt(0);
			errors.stream().forEach(error -> {
				Row row = sheet.getRow(error.getLine());
				List<String> messages = error.getErrors();
				List<String> messageFormat = messages.stream().map(p -> extractError(p)).collect(Collectors.toList());
				Cell cell = row.createCell(20);
				cell.setCellValue(messageFormat.toString());
			});
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this.file;
	}
	private String extractError(String error) {
		String[] array = error.split(":");
		return array[2].replace("[", "").replace("]", "");
	}
	
	

}
