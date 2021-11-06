package click.escuela.admin.core.connector;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.admin.core.exception.EmailException;
import click.escuela.admin.core.exception.ProcessException;
import click.escuela.admin.core.feign.ProcessorController;
import click.escuela.admin.core.provider.processor.dto.ProcessDTO;
import click.escuela.admin.core.provider.processor.dto.ResponseCreateProcessDTO;
import click.escuela.admin.core.provider.student.dto.FileError;

@Service
public class ProcessorConnector {

	@Autowired
	private ProcessorController processorController;

	public ResponseCreateProcessDTO create(String name, String schoolId, MultipartFile file) throws ProcessException {
		
		return processorController.saveAndRead(file,name,schoolId);
	}
	
	public String update(String processId, String schoolId, List<FileError> errors, String status) {	
		return processorController.update( schoolId, processId, errors, status);
	}
	
	
	public List<ProcessDTO> getBySchoolId(String schoolId) {	
		return processorController.getBySchoolId(schoolId);
	}
	
	public byte[] getFileById(String schoolId, String processId) throws IOException {	
		return processorController.getFileById(schoolId,processId);
	}
	
	public void sendEmail(String password, String userName, String email, String schoolId) throws EmailException {
		processorController.sendEmail(password, userName, email, schoolId);
	}
}
