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

	@Operation(summary = "Create Course", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> create(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated CourseApi courseApi) throws TransactionException {

		courseService.create(schoolId, courseApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseEnum.CREATE_OK);
	}

	@Operation(summary = "Update/Add course in student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idCourse}/student/add/{idStudent}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addStudent(@PathVariable("schoolId")String schoolId,@PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException {
		courseService.addStudent(schoolId,idCourse, idStudent);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseEnum.UPDATE_OK);
	}

	@Operation(summary = "Update/Delete course in student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idCourse}/student/del/{idStudent}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> deleteStudent(@PathVariable("schoolId")String schoolId,@PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException {
		courseService.deleteStudent(schoolId,idCourse, idStudent);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseEnum.UPDATE_OK);
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
