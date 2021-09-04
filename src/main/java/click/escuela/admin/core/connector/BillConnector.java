package click.escuela.admin.core.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.feign.StudentController;
import click.escuela.admin.core.provider.student.api.BillApi;
import click.escuela.admin.core.provider.student.api.BillStatusApi;
import click.escuela.admin.core.provider.student.dto.BillDTO;

@Service
public class BillConnector {

	@Autowired
	private StudentController billController;

	public void create(String schoolId, String studentId, BillApi billApi) throws TransactionException {
		billController.createBill(schoolId, studentId, billApi);
	}

	public List<BillDTO> getByStudentId(String schoolId, String studentId, String status, Integer month, Integer year) {
		return billController.getByStudentId(schoolId, studentId, status, month, year);
	}
	
	public void updatePayment(String schoolId, String billId, BillStatusApi billApi) throws TransactionException {
		billController.updatePayment(schoolId, billId, billApi);
	}

}
