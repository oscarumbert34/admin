package click.escuela.admin.core.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.admin.core.enumator.BillEnum;
import click.escuela.admin.core.enumator.BillMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.BillApi;
import click.escuela.admin.core.provider.student.api.BillStatusApi;
import click.escuela.admin.core.provider.student.dto.BillDTO;
import click.escuela.admin.core.provider.student.service.impl.BillServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/bill")
public class BillController {

	@Autowired
	private BillServiceImpl billService;

	@Operation(summary = "Create Bill", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BillMessage> create(@PathVariable("schoolId") String schoolId,
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestBody @Validated BillApi billApi) throws TransactionException {

		billService.create(schoolId, studentId, billApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(BillMessage.CREATE_OK);
	}

	@Operation(summary = "Get bill by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillDTO.class))) })
	@GetMapping(value = "student/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BillDTO>> getByStudentId(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestParam(required = false, value = "status") String status,
			@RequestParam(required = false, value = "month") Integer month,
			@RequestParam(required = false, value = "year") Integer year) {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(billService.getByStudentId(schoolId, studentId, status, month, year));
	}
	
	@Operation(summary = "Update Payment Bill", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{billId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BillEnum> updatePayment(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Bill id", required = true) @PathVariable("billId") String billId, @RequestBody @Validated BillStatusApi billStatusApi) throws TransactionException {
		billService.updatePayment(schoolId, billId,billStatusApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(BillEnum.PAYMENT_STATUS_CHANGED);
	}
}
