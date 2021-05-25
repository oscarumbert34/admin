package click.escuela.admin.core.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.BillApi;
import click.escuela.admin.core.provider.student.api.CourseApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.dto.StudentDTO;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;
import io.swagger.v3.oas.annotations.Parameter;

@FeignClient(name = "students", url = "localhost:8090")
public interface StudentController {

	// StudentController
	@GetMapping(value = "/click-escuela/school-admin/school/{schoolId}/student/{studentId}")
	public StudentDTO getById(@PathVariable("schoolId") String schoolId, @PathVariable("studentId") String studentId,
			@RequestParam("fullDetail") Boolean fullDetail) throws TransactionException;

	@GetMapping(value = "/click-escuela/school-admin/school/{schoolId}/student")
	public List<StudentDTO> getBySchool(@PathVariable("schoolId") String schoolId,
			@RequestParam("fullDetail") Boolean fullDetail) throws TransactionException;

	@GetMapping(value = "/click-escuela/school-admin/school/{schoolId}/student/course/{courseId}")
	public List<StudentDTO> getByCourse(@PathVariable("schoolId") String schoolId,
			@PathVariable("courseId") String courseId, @RequestParam("fullDetail") Boolean fullDetail)
			throws TransactionException;

	@PostMapping(value = "/click-escuela/school-admin/school/{schoolId}/student")
	public String createStudent(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws TransactionException;

	@PutMapping(value = "/click-escuela/school-admin/school/{schoolId}/student")
	public String updateStudent(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws TransactionException;

	// CourseController
	@PostMapping(value = "/click-escuela/school-admin/school/{schoolId}/course")
	public String createCourse(@PathVariable("schoolId") String schoolId, @RequestBody @Validated CourseApi courseApi)
			throws TransactionException;

	@PutMapping(value = "/click-escuela/school-admin/school/{schoolId}/course/{idCourse}/student/add/{idStudent}")
	public String addStudent(@PathVariable("schoolId") String schoolId, @PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException;

	@PutMapping(value = "/click-escuela/school-admin/school/{schoolId}/course/{idCourse}/student/del/{idStudent}")
	public String deleteStudent(@PathVariable("schoolId") String schoolId, @PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException;

	// BillController
	@PostMapping(value = "/click-escuela/school-admin/school/{schoolId}/bill/{studentId}")
	public String createBill(@PathVariable("schoolId") String schoolId,
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestBody @Validated BillApi billApi) throws TransactionException;

	// TeacherController
	@PostMapping(value = "/click-escuela/school-admin/school/{schoolId}/teacher")
	public String createTeacher(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TransactionException;
	
	@PutMapping(value = "/click-escuela/school-admin/school/{schoolId}/teacher")
	public String updateTeacher(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TransactionException;
	
	@GetMapping(value = "/click-escuela/school-admin/school/{schoolId}/teacher/{teacherId}")
	public TeacherDTO getByTeacherId(@PathVariable("schoolId") String schoolId, @PathVariable("teacherId") String teacherId) throws TransactionException;

	@GetMapping(value = "/click-escuela/school-admin/school/{schoolId}/teacher")
	public List<TeacherDTO> getBySchoolId(@PathVariable("schoolId") String schoolId);

	@GetMapping(value = "/click-escuela/school-admin/school/{schoolId}/teacher/course/{courseId}")
	public List<TeacherDTO> getByCourseId(@PathVariable("schoolId") String schoolId,
			@PathVariable("courseId") String courseId);


}
