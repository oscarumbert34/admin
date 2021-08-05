package click.escuela.admin.core.rest;

import java.io.File;

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

import click.escuela.admin.core.enumator.ExcelMessage;
import click.escuela.admin.core.exception.ExcelException;
import click.escuela.admin.core.provider.student.api.ExcelApi;
import click.escuela.admin.core.provider.student.service.impl.ExcelServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/excel")
public class ExcelController {

	@Autowired
	private ExcelServiceImpl excelService;

	@Operation(summary = "Save Excel", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ExcelMessage> create(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated File file) throws Exception {
		excelService.save(schoolId, file);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ExcelMessage.CREATE_OK);
	}

}
