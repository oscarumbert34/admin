package click.escuela.admin.core.provider.student.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.ProcessorConnector;
import click.escuela.admin.core.exception.EmailException;

@Service
public class EmailServiceImpl {
	
	@Autowired
	private ProcessorConnector processorConnector;
	
	public void sendEmail(String password, String userName, String email, String schoolId) throws EmailException {
		processorConnector.sendEmail(password, userName, email, schoolId);
	}

}
