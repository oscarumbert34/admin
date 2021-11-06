package click.escuela.admin.core.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.admin.core.enumator.StudentMessage;
import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.dto.StudentDTO;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
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
	public ResponseEntity<List<StudentDTO>> getBySchool(@PathVariable("schoolId") String schoolId,
			@RequestParam("fullDetail") Boolean fullDetail) throws TransactionException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.getBySchool(schoolId, fullDetail));
	}

	@Operation(summary = "Get student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping("/{studentId}")
	public ResponseEntity<StudentDTO> getById(@PathVariable("schoolId") String schoolId,
			@PathVariable("studentId") String studentId, @RequestParam("fullDetail") Boolean fullDetail)
			throws TransactionException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.getById(schoolId, studentId, fullDetail));
	}

	@Operation(summary = "Get student by courseId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(value = "course/{courseId}")
	public ResponseEntity<List<StudentDTO>> getByCourse(@PathVariable("schoolId") String schoolId,
			@PathVariable("courseId") String courseId, @RequestParam("fullDetail") Boolean fullDetail)
			throws TransactionException {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(studentService.getByCourse(schoolId, courseId, fullDetail));
	}

	/*@GetMapping(value = "prueba")
	public ResponseEntity<String> prueba()
			throws TransactionException {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body("prueba exitosa");
	}*/

	@Operation(summary = "Create student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<StudentMessage> create(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws TransactionException, SchoolException {
		studentService.create(schoolId, studentApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentMessage.CREATE_OK);
	}

	@Operation(summary = "Update student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<StudentMessage> update(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws TransactionException {
		studentService.update(schoolId, studentApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentMessage.UPDATE_OK);
	}

	@DeleteMapping(value = "/{schoolId}")
	public ResponseEntity<?> delete() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
}
