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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.dto.CourseDTO;
import click.escuela.admin.core.provider.student.service.impl.CourseServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value = "/courses")
@Validated
public class CourseController {

	@Autowired
	private CourseServiceImpl courseService;

	@ApiOperation(value = "Get students by courseId", response = CourseDTO.class, notes = "Get students by courseId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Students"),
			@ApiResponse(code = 401, message = "Students") })
	@GetMapping(value = "/{courseId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getStudents(
			@ApiParam(value = "Course id", required = true) @RequestParam("courseId") String courseId) throws TransactionException {
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(courseService.getByCourse(courseId));
	}

	@ApiOperation(value = "Create a Course", response = CourseDTO.class, notes = "Create a Course")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Students"),
			@ApiResponse(code = 401, message = "Students") })
	@PostMapping(value = "")
	public ResponseEntity<?> create() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
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
