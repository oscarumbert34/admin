package click.escuela.admin.core.service.impl;

import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.provider.student.api.StudentApiFile;
import click.escuela.admin.core.provider.student.dto.FileError;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
import click.escuela.admin.core.service.BulkUpload;

@Service
public class StudentBulkUpload implements BulkUpload<StudentApiFile> {

	@Autowired
	private StudentServiceImpl studentService;

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

}
