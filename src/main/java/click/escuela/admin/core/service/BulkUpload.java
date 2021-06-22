package click.escuela.admin.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import click.escuela.admin.core.provider.student.dto.FileError;

public interface BulkUpload <T>{

	public List<T> readFile(File file) throws FileNotFoundException, IOException;
	public List<FileError> upload(List<T> list);
}
