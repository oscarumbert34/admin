package click.escuela.admin.core.service;


import java.util.List;


import click.escuela.admin.core.provider.student.dto.FileError;

public interface BulkUpload <T>{

	public List<FileError> upload(String schoolId, List<T> list);
	
}
