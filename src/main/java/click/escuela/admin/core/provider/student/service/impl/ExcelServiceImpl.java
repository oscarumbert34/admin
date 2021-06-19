package click.escuela.admin.core.provider.student.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.ExcelConnector;
import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.provider.student.api.ExcelApi;

@Service
public class ExcelServiceImpl {

	@Autowired
	private ExcelConnector excelConnector;

	public void save(String schoolId, ExcelApi excelApi) throws ExcelException {
		excelConnector.save(schoolId, excelApi);
	}

}
