package click.escuela.admin.core.provider.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.connector.BillConnector;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.BillApi;
import click.escuela.admin.core.provider.student.api.BillStatusApi;
import click.escuela.admin.core.provider.student.dto.BillDTO;

@Service
public class BillServiceImpl {

	@Autowired
	private BillConnector billConnector;

	public void create(String schoolId, String studentId, BillApi billApi) throws TransactionException {
		billConnector.create(schoolId,studentId, billApi);
	}
	
	public List<BillDTO> getByStudentId(String schoolId, String studentId, String status, Integer month, Integer year) {
		return billConnector.getByStudentId(schoolId, studentId, status, month, year);
	}

	public void updatePayment(String schoolId, String billId, BillStatusApi billApi) throws TransactionException {
		billConnector.updatePayment(schoolId, billId, billApi);
	}
}
