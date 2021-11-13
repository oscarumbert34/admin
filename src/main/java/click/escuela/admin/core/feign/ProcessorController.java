package click.escuela.admin.core.feign;

import java.io.IOException;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.admin.core.configuration.FeignInterceptor;
import click.escuela.admin.core.exception.EmailException;
import click.escuela.admin.core.exception.ProcessException;
import click.escuela.admin.core.provider.processor.dto.ProcessDTO;
import click.escuela.admin.core.provider.processor.dto.ResponseCreateProcessDTO;
import click.escuela.admin.core.provider.student.dto.FileError;
import io.swagger.v3.oas.annotations.Parameter;

@FeignClient(name = "processor", url = "localhost:8093/click-escuela/processor", configuration = FeignInterceptor.class)
public interface ProcessorController {

	@PostMapping(value = "/school/{schoolId}/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseCreateProcessDTO saveAndRead(@RequestPart(value = "file") MultipartFile file,
			@Parameter(name = "Name", required = true) @RequestParam("name") String name,
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId)
			throws ProcessException;

	@PutMapping(value = "/school/{schoolId}/process/{processId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public String update(@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,

			@Parameter(name = "Process id", required = true) @PathVariable("processId") String processId,
			@RequestBody List<FileError> errors,
			@Parameter(name = "status", required = true) @RequestParam("status") String status);

	@GetMapping(value = "/school/{schoolId}/process", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<ProcessDTO> getBySchoolId(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId);

	@GetMapping(value = "/school/{schoolId}/process/{processId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public byte[] getFileById(@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@PathVariable("processId") String processId) throws IOException;

	// EmailController
	@PostMapping(value = "/school/{schoolId}/email")
	public String sendEmail(@RequestParam(value = "password") String password,
			@RequestParam("userName") String userName, @RequestParam("email") String email,
			@PathVariable("schoolId") String schoolId) throws EmailException;

}
