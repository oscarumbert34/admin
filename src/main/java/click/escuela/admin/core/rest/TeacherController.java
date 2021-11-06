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
import org.springframework.web.bind.annotation.RestController;

import click.escuela.admin.core.enumator.TeacherMessage;
import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;
import click.escuela.admin.core.provider.student.service.impl.TeacherServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/teacher")
public class TeacherController {

	@Autowired
	private TeacherServiceImpl teacherService;

	@Operation(summary = "Get teachers by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))) })
	@GetMapping(value = "")
	public ResponseEntity<List<TeacherDTO>> getBySchoolId(@PathVariable("schoolId") String schoolId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacherService.getBySchoolId(schoolId));
	}

	@Operation(summary = "Get teacher by id", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))) })
	@GetMapping("/{teacherId}")
	public ResponseEntity<TeacherDTO> getById(@PathVariable("schoolId") String schoolId,
			@PathVariable("teacherId") String teacherId) throws TransactionException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacherService.getById(schoolId, teacherId));
	}

	@Operation(summary = "Get teachers by courseId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))) })
	@GetMapping(value = "course/{courseId}")
	public ResponseEntity<List<TeacherDTO>> getByCourseId(@PathVariable("schoolId") String schoolId,
			@PathVariable("courseId") String courseId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacherService.getByCourseId(schoolId, courseId));
	}

	@Operation(summary = "Create Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherMessage> create(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TransactionException, SchoolException {
		teacherService.create(schoolId, teacherApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.CREATE_OK);
	}

	@Operation(summary = "Update Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherMessage> update(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TransactionException {
		teacherService.update(schoolId, teacherApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}

	@Operation(summary = "Add courses in Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idTeacher}/add/courses")
	public ResponseEntity<TeacherMessage> addCourses(@PathVariable("schoolId") String schoolId,
			@PathVariable("idTeacher") String idTeacher, @RequestBody @Validated List<String> listUUIDs)
			throws TransactionException {
		teacherService.addCourses(schoolId, idTeacher, listUUIDs);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}

	@Operation(summary = "Delete courses from Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idTeacher}/del/courses")
	public ResponseEntity<TeacherMessage> deleteCourses(@PathVariable("schoolId") String schoolId,
			@PathVariable("idTeacher") String idTeacher, @RequestBody @Validated List<String> listUUIDs)
			throws TransactionException {
		teacherService.deleteCourses(schoolId, idTeacher, listUUIDs);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}
}
