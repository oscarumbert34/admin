package click.escuela.admin.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.admin.core.enumator.TeacherMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.service.impl.TeacherServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/teacher")
public class TeacherController {

	@Autowired
	private TeacherServiceImpl teacherService;

	@Operation(summary = "Create Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherMessage> create(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TransactionException {

		teacherService.create(schoolId, teacherApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.CREATE_OK);
	}
}
