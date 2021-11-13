package click.escuela.admin.core.provider.processor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.provider.student.api.UserApi;
import click.escuela.admin.core.provider.student.connector.SecurityConnector;

@Service
public class SecurityServiceImpl {

	@Autowired
	private SecurityConnector securityConnector;
	
	public UserApi saveUser(UserApi userApi) throws SchoolException {
		return securityConnector.saveUser(userApi);
	}
}
