package click.escuela.admin.core.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.StudentUpdateApi;

@FeignClient(name = "students", url = "localhost:8090")
public interface StudentController {

	@GetMapping(value = "/click-escuela/student-core/school/{schoolId}/student")
	public List getBySchool(@PathVariable("schoolId") String schoolId) throws TransactionException;
	
	@PostMapping(value = "/click-escuela/student-core/school/{schoolId}/student")
	public String create(@PathVariable("schoolId") String schoolId, @RequestBody @Validated StudentApi studentApi) throws TransactionException;

  @PutMapping(value = "/click-escuela/student-core/school/{schoolId}/student")
	public String update(@PathVariable("schoolId") String schoolId,@RequestBody @Validated StudentUpdateApi studentUpdateApi) throws TransactionException;
	
}
