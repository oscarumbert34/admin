package click.escuela.admin.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.admin.core.api.CourseApi;
import click.escuela.admin.core.enumator.CourseEnum;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.dto.CourseDTO;
import click.escuela.admin.core.provider.student.service.impl.CourseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/course")
public class CourseController {

	@Autowired
	private CourseServiceImpl courseService;

	//Metodo de prueba
	@Operation(summary = "Get all the courses", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllCourses() throws TransactionException {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(courseService.findAll());
	}

	@Operation(summary = "Create Course", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> create(
		 @RequestBody @Validated CourseApi courseApi) throws TransactionException {
		
		courseService.create(courseApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseEnum.CREATE_OK);
	}

	@PutMapping(value = "/{courseId}")
	public ResponseEntity<?> update() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@DeleteMapping(value = "/{courseId}")
	public ResponseEntity<?> delete() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
}
