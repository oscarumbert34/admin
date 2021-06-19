package click.escuela.admin.core.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.ExcelApi;

@Service
public class ExcelConnector {

	@Autowired
	private StudentController excelController;

	public void save(String schoolId, ExcelApi excelApi) throws ExcelException {
		excelController.saveExcel(schoolId, excelApi);
	}

}
