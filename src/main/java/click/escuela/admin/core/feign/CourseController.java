package click.escuela.admin.core.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import click.escuela.admin.core.api.CourseApi;
import click.escuela.admin.core.exception.TransactionException;

@FeignClient(name = "students", url = "localhost:8090")
public interface CourseController {

	@GetMapping(value = "/click-escuela/student-core/school/{schoolId}/course/getAll")
	public List getAllCourses(@PathVariable("schoolId") String schoolId) throws TransactionException;
	
	@PostMapping(value = "/click-escuela/student-core/school/{schoolId}/course")
	public String create(@PathVariable("schoolId") String schoolId,@RequestBody @Validated CourseApi courseApi) throws TransactionException;
	
}
