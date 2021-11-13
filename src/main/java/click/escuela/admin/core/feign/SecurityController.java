package click.escuela.admin.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.admin.core.configuration.FeignInterceptor;
import click.escuela.admin.core.exception.ProcessException;
import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.provider.processor.dto.ResponseCreateProcessDTO;
import click.escuela.admin.core.provider.student.api.UserApi;
import io.swagger.v3.oas.annotations.Parameter;

					 
@FeignClient(name = "security", url="localhost:8093/click-escuela/security", configuration = FeignInterceptor.class)
public interface SecurityController{
	
	@PostMapping(value="/school/{schoolId}/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseCreateProcessDTO saveAndRead(@RequestPart(value = "file") MultipartFile file,
			@Parameter(name = "Name", required = true) @RequestParam("name") String name,
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId) throws ProcessException;
	
	@PostMapping(value="/users")
	public UserApi saveUser(@RequestBody UserApi userApi) throws SchoolException;
}
