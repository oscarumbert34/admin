package click.escuela.admin.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	@Override
	public List<StudentApiFile> readFile(File file) throws EncryptedDocumentException, IOException{
			InputStream inputStream = new FileInputStream(file);//NOSONAR
			
			Workbook wb = WorkbookFactory.create(inputStream);
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
			wb.close();
			return students;			
	}

	private StudentApiFile buildStudentApi(Row row) {
		StudentApiFile student = new StudentApiFile();
		
		Cell name = row.getCell(0);
		if (name != null)
			student.setName(name.getStringCellValue());
		Cell surname = row.getCell(1);
		if (surname != null)
			student.setSurname(surname.getStringCellValue());
		Cell document = row.getCell(2);
		if (document != null)
			student.setDocument(document.getStringCellValue());
		Cell gender = row.getCell(3);
		if (gender != null)
			student.setGender(getGender(gender.getStringCellValue()));
		Cell birthday = row.getCell(4);
		if (birthday != null) {
			String date = birthday.getStringCellValue();
			if(!date.isEmpty()) {
				student.setBirthday(LocalDate.parse(date));
			}
		}
		Cell cellPhone = row.getCell(5);
		if (cellPhone != null)
			student.setCellPhone(cellPhone.getStringCellValue());
		Cell email = row.getCell(6);
		if (email != null)
			student.setEmail(email.getStringCellValue());
		Cell grade = row.getCell(7);
		if (grade != null)
			student.setGrade(grade.getStringCellValue());
		Cell division = row.getCell(8);
		if (division != null)
			student.setDivision(division.getStringCellValue());
		Cell level = row.getCell(9);
		if (level != null)
			student.setLevel(getLevel(level.getStringCellValue()));

		student.setLine(row.getRowNum());
		
		student.setAdressApi(getAdress(row));
		student.setParentApi(getParent(row));

		return student;
	}

	private ParentApi getParent(Row row) {
		ParentApi parent = new ParentApi();
		Cell parentName = row.getCell(13);
		if (parentName != null)
			parent.setName(parentName.getStringCellValue());
		Cell parentSurname = row.getCell(14);
		if (parentSurname != null)
			parent.setSurname(parentSurname.getStringCellValue());
		Cell parentDocument = row.getCell(15);
		if (parentDocument != null)
			parent.setDocument(parentDocument.getStringCellValue());
		Cell parentGender = row.getCell(16);
		if (parentGender != null)
			parent.setGender(getGender(parentGender.getStringCellValue()));
		Cell parentBirthday = row.getCell(17);
		if (parentBirthday != null) {
			String date = parentBirthday.getStringCellValue();
			if(!date.isEmpty()) {
				parent.setBirthday(LocalDate.parse(date));
			}
		}
		Cell parentCellPhone = row.getCell(18);
		if (parentCellPhone != null)
			parent.setCellPhone(parentCellPhone.getStringCellValue());
		Cell parentEmail = row.getCell(19);
		if (parentEmail != null)
			parent.setEmail(parentEmail.getStringCellValue());
		
		parent.setAdressApi(getAdress(row));
		return parent;
	}

	private AdressApi getAdress(Row row) {
		AdressApi adress = new AdressApi();
		Cell street = row.getCell(10);
		if (street != null)
			adress.setStreet(street.getStringCellValue());
		Cell number = row.getCell(11);
		if (number != null)
			adress.setNumber(number.getStringCellValue());
		Cell locality = row.getCell(12);
		if (locality != null)
			adress.setLocality(locality.getStringCellValue());
		return adress;
	}

	private String getGender(String gender) {
		if (gender.equals("Masculino")) {
			gender = GenderType.MALE.toString();
		} else {
			gender = GenderType.FEMALE.toString();
		}
		return gender;
	}

	private String getLevel(String level) {
		if (level.equals("Preescolar")) {
			level = EducationLevels.PREESCOLAR.toString();
		} else if (level.equals("Primario")) {
			level = EducationLevels.PRIMARIO.toString();
		} else if (level.equals("Secundario")) {
			level = EducationLevels.SECUNDARIO.toString();
		} else if (level.equals("Terciario")) {
			level = EducationLevels.TERCIARIO.toString();
		}
		return level;
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
	public File writeErrors(List<FileError> errors, File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);//NOSONAR
		try (Workbook wb = WorkbookFactory.create(inputStream)){
			Sheet sheet = wb.getSheetAt(0);
			if(!errors.isEmpty()) {
				errors.stream().forEach(error -> {
					Row row = sheet.getRow(error.getLine());
					List<String> messages = error.getErrors();
					Cell cell = row.createCell(20);
					cell.setCellValue(messages.toString());
				});
				OutputStream outputStream = new FileOutputStream(file);//NOSONAR
				wb.write(outputStream);
			}
		} catch (IOException e) {
			throw new IOException("No se pudo escribir los errores en el arvhivo");
		}
		
		return file;
	}

}
