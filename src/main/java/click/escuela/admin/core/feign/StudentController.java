package click.escuela.admin.core.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import click.escuela.admin.core.enumator.BillMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.BillApi;
import click.escuela.admin.core.provider.student.api.BillStatusApi;
import click.escuela.admin.core.provider.student.api.CourseApi;

import click.escuela.admin.core.provider.student.api.SchoolApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.dto.BillDTO;
import click.escuela.admin.core.provider.student.dto.StudentDTO;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;
import io.swagger.v3.oas.annotations.Parameter;

@FeignClient(name = "school-admin")
public interface StudentController {

	// StudentController
	@GetMapping(value = "/school/{schoolId}/student/{studentId}")
	public StudentDTO getById(@PathVariable("schoolId") String schoolId, @PathVariable("studentId") String studentId,
			@RequestParam("fullDetail") Boolean fullDetail) throws TransactionException;

	@GetMapping(value = "/school/{schoolId}/student")
	public List<StudentDTO> getBySchool(@PathVariable("schoolId") String schoolId,
			@RequestParam("fullDetail") Boolean fullDetail) throws TransactionException;

	@GetMapping(value = "/school/{schoolId}/student/course/{courseId}")
	public List<StudentDTO> getByCourse(@PathVariable("schoolId") String schoolId,
			@PathVariable("courseId") String courseId, @RequestParam("fullDetail") Boolean fullDetail)
			throws TransactionException;

	@PostMapping(value = "/school/{schoolId}/student")
	public String createStudent(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws TransactionException;

	@PutMapping(value = "/school/{schoolId}/student")
	public String updateStudent(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws TransactionException;

	// CourseController
	@PostMapping(value = "/school/{schoolId}/course")
	public String createCourse(@PathVariable("schoolId") String schoolId, @RequestBody @Validated CourseApi courseApi)
			throws TransactionException;

	@PutMapping(value = "/school/{schoolId}/course/{idCourse}/student/add/{idStudent}")
	public String addStudent(@PathVariable("schoolId") String schoolId, @PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException;

	@PutMapping(value = "/school/{schoolId}/course/{idCourse}/student/del/{idStudent}")
	public String deleteStudent(@PathVariable("schoolId") String schoolId, @PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException;
	
	// BillController
	@PostMapping(value = "/school/{schoolId}/bill/{studentId}")
	public String createBill(@PathVariable("schoolId") String schoolId,
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestBody @Validated BillApi billApi) throws TransactionException;

	@GetMapping(value = "/school/{schoolId}/bill/student/{studentId}")
	public List<BillDTO> getByStudentId(@PathVariable("schoolId") String schoolId,
			@PathVariable("studentId") String studentId,
			@RequestParam(required = false, value = "status") String status,
			@RequestParam(required = false, value = "month") Integer month,
			@RequestParam(required = false, value = "year") Integer year);
	
	@PutMapping(value = "/school/{schoolId}/bill/{billId}")
	public ResponseEntity<BillMessage> updatePayment(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Bill id", required = true) @PathVariable("billId") String billId,
			@RequestBody @Validated BillStatusApi billStatusApi) throws TransactionException;

	// TeacherController
	@PostMapping(value = "/school/{schoolId}/teacher")
	public String createTeacher(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TransactionException;

	@PutMapping(value = "/school/{schoolId}/teacher")
	public String updateTeacher(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TransactionException;

	@GetMapping(value = "/school/{schoolId}/teacher/{teacherId}")
	public TeacherDTO getByTeacherId(@PathVariable("schoolId") String schoolId,
			@PathVariable("teacherId") String teacherId) throws TransactionException;

	@GetMapping(value = "/school/{schoolId}/teacher")
	public List<TeacherDTO> getBySchoolId(@PathVariable("schoolId") String schoolId);

	@GetMapping(value = "/school/{schoolId}/teacher/course/{courseId}")
	public List<TeacherDTO> getByCourseId(@PathVariable("schoolId") String schoolId,
			@PathVariable("courseId") String courseId);
	
	@PutMapping(value = "/click-escuela/school-admin/school/{schoolId}/teacher/{idTeacher}/add/courses")
	public String addCourses(@PathVariable("schoolId") String schoolId, @PathVariable("idTeacher") String idTeacher,
			@RequestBody @Validated List<String> listUUIDs) throws TransactionException;

	@PutMapping(value = "/click-escuela/school-admin/school/{schoolId}/teacher/{idTeacher}/del/courses")
	public String deleteCourses(@PathVariable("schoolId") String schoolId, @PathVariable("idTeacher") String idTeacher,
			@RequestBody @Validated List<String> listUUIDs) throws TransactionException;

	// SchoolController
	@PostMapping(value = "/school")
	public String createSchool(@RequestBody @Validated SchoolApi schoolApi) throws TransactionException;


}
