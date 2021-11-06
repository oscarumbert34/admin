package click.escuela.admin.core.provider.student.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.feign.SecurityController;
import click.escuela.admin.core.provider.student.api.UserApi;

@Service
public class SecurityConnector {
	
	@Autowired
	private SecurityController securityController;
	
	public UserApi saveUser(UserApi userApi) throws SchoolException {
		return securityController.saveUser(userApi);
	}

}
