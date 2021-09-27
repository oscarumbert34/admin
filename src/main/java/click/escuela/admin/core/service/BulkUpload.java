package click.escuela.admin.core.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;

import click.escuela.admin.core.provider.student.dto.FileError;

public interface BulkUpload <T>{

	public List<FileError> upload(String schoolId, List<T> list);
	
}
