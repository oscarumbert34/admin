package click.escuela.admin.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.admin.core.enumator.StudentEnum;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
import click.escuela.student.api.StudentApi;
import click.escuela.student.api.StudentUpdateApi;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.exception.TransactionException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/student")
public class StudentController {

	@Autowired
	private StudentServiceImpl studentService;
	
	@Operation(summary = "Get student by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
 	@GetMapping(value = "")	
	public ResponseEntity<?> getBySchool(@PathVariable("schoolId") String schoolId) throws TransactionException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.getBySchool(schoolId));
	}

	@Operation(summary = "Create student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> create(@RequestBody @Validated StudentApi studentApi) throws TransactionException {
		studentService.create(studentApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentEnum.CREATE_OK);
	}
	

	@Operation(summary = "Update student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update( 
			@RequestBody @Validated StudentUpdateApi studentUpdateApi) throws TransactionException {
		studentService.update(studentUpdateApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentEnum.UPDATE_OK);
	}

 	@DeleteMapping(value = "/{schoolId}")	
 	public ResponseEntity<?> delete() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
}
