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
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
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

	private String getCell(Row row, int index) {
		Cell cell = row.getCell(index);
		return Objects.isNull(cell) ? StringUtils.EMPTY : cell.getStringCellValue(); 
	}
	private StudentApiFile buildStudentApi(Row row) {
		StudentApiFile student = new StudentApiFile();
		
		student.setName(getCell(row, 0));
		student.setSurname(getCell(row, 1));
		student.setDocument(getCell(row, 2));
		student.setGender(getGender(row, 3));
		student.setBirthday(getDate(row, 4));
		student.setCellPhone(getCell(row, 5));
		student.setEmail(getCell(row, 6));
		student.setGrade(getCell(row, 7));
		student.setDivision(getCell(row, 8));
		student.setLevel(getLevel(row,9));

		student.setLine(row.getRowNum());
		
		student.setAdressApi(getAdress(row));
		student.setParentApi(getParent(row));

		return student;
	}

	private ParentApi getParent(Row row) {
		ParentApi parent = new ParentApi();
		
		parent.setName(getCell(row, 13));
		parent.setSurname(getCell(row, 14));
		parent.setDocument(getCell(row, 15));
		parent.setGender(getGender(row, 16));
		parent.setBirthday(getDate(row, 17));
		parent.setCellPhone(getCell(row, 18));
		parent.setEmail(getCell(row, 19));
		parent.setAdressApi(getAdress(row));
		
		return parent;
	}

	private AdressApi getAdress(Row row) {
		AdressApi adress = new AdressApi();
		
		adress.setStreet(getCell(row, 10));
		adress.setNumber(getCell(row, 11));
		adress.setLocality(getCell(row, 12));

		return adress;
	}

	private LocalDate getDate(Row row, int index) {
		Cell cell = row.getCell(index);
		LocalDate localDate = null;
		
		if (cell != null) {
			String date = cell.getStringCellValue();
			
			if(!date.isEmpty()) {
				localDate = LocalDate.parse(date);
			}
		}
		return localDate;
	} 
	private String getGender(Row row, int index) {
		Cell cell = row.getCell(index);
		String gender = StringUtils.EMPTY;
		
		if (cell != null)
			gender = getGenderType(cell.getStringCellValue());
		
		return gender;
			
	}
	private String getGenderType(String gender) {
		
		if (gender.equals("Masculino")) {
			gender = GenderType.MALE.toString();
		}else{
			gender = GenderType.FEMALE.toString();
		}
		return gender;
	}

	private String getLevel(Row row, int index) {
		Cell cell = row.getCell(index);
		String level = StringUtils.EMPTY;
		
		if (cell != null)
			level = getEducationLevel(cell.getStringCellValue());
		
		return level;
	}
	private String getEducationLevel(String level) {
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
