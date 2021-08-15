package click.escuela.admin.core.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;

import click.escuela.admin.core.provider.student.dto.FileError;

public interface BulkUpload <T>{

	public List<T> readFile(File file) throws EncryptedDocumentException, IOException;
	public List<FileError> upload(String schoolId, List<T> list);
	public File writeErrors(List<FileError> errors, File file) throws EncryptedDocumentException, IOException;
	
}
