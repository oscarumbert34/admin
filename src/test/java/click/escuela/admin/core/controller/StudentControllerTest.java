package click.escuela.admin.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.enumator.StudentMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.StudentUpdateApi;
import click.escuela.admin.core.provider.student.dto.StudentDTO;
import click.escuela.admin.core.provider.student.service.impl.CourseServiceImpl;
import click.escuela.admin.core.provider.student.service.impl.StudentServiceImpl;
import click.escuela.admin.core.rest.StudentController;
import click.escuela.admin.core.rest.handler.Handler;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private StudentController studentController;

	@Mock
	private StudentServiceImpl studentService;

	@Mock
	private CourseServiceImpl courseService;

	private ObjectMapper mapper;
	private StudentApi studentApi;
	private StudentUpdateApi studentUpdateApi;
	private ParentApi parentApi;
	private AdressApi adressApi;
	private UUID studentId;
	private UUID idCourse;
	private Integer idSchool;
	private Boolean fullDetail;

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(studentController, "studentService", studentService);

		idCourse = UUID.randomUUID();
		idSchool = 1234;
		studentId = UUID.randomUUID();
		fullDetail = false;
		adressApi = new AdressApi("Calle falsa", "6458", "Nogues");
		parentApi = ParentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).cellPhone("3534543")
				.document("33543534").email("oscar.umnbetrqgmail.com").gender("FEMALE").name("oscar").surname("umbert")
				.build();

		studentApi = StudentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).document("32333222")
				.cellPhone("4534543").division("C").grade("3°").email("oscar@gmail.com").level("SECUNDARIO")
				.gender("Male").name("oscar").surname("umbert").parentApi(parentApi).schoolId(1234).build();

		studentUpdateApi = new StudentUpdateApi(studentApi);
		studentUpdateApi.setId(UUID.randomUUID().toString());

		doNothing().when(studentService).create(Mockito.any());
		// doNothing().when(studentService).update(Mockito.any());

	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentMessage.CREATE_OK.name());

	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {

		doThrow(new TransactionException(StudentMessage.CREATE_ERROR.getCode(), StudentMessage.CREATE_ERROR.getDescription()))
				.when(studentService).create(Mockito.any());

		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentMessage.CREATE_ERROR.getDescription());

	}

	@Test
	public void whenUpdatOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentMessage.UPDATE_OK.name());

	}

	@Test
	public void whenUpdateErrorService() throws JsonProcessingException, Exception {

		doThrow(new TransactionException(StudentMessage.UPDATE_ERROR.getCode(), StudentMessage.UPDATE_ERROR.getDescription()))
				.when(studentService).update(Mockito.any());

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentMessage.UPDATE_ERROR.getDescription());

	}

	@Test
	public void getStudentById() throws JsonProcessingException, Exception {
		StudentDTO studentDTO = StudentDTO.builder().id(studentId.toString()).name("Oscar").build();

		Mockito.when(studentService.getById(idSchool.toString(), studentId.toString(), fullDetail))
				.thenReturn(studentDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders
						.get("/school/{schoolId}/student/{idStudent}?fullDetail=false", "1234", studentId.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		StudentDTO response = mapper.readValue(result.getResponse().getContentAsString(), StudentDTO.class);
		assertThat(response).hasFieldOrPropertyWithValue("id", studentId.toString());
	}

	@Test
	public void getStudentByIdIsError() throws JsonProcessingException, Exception {
		studentId = UUID.randomUUID();
		doThrow(new TransactionException(StudentMessage.GET_ERROR.getCode(), StudentMessage.GET_ERROR.getDescription()))
				.when(studentService).getById(idSchool.toString(), studentId.toString(), fullDetail);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/student/{idStudent}?fullDetail=false",
						idSchool.toString(), studentId.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void getStudentByIdSchool() throws JsonProcessingException, Exception {
		StudentDTO studentDTO = StudentDTO.builder().id(studentId.toString()).name("Oscar").build();
		List<StudentDTO> students = new ArrayList<>();
		students.add(studentDTO);

		Mockito.when(studentService.getBySchool(idSchool.toString(), fullDetail)).thenReturn(students);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/student?fullDetail=false", idSchool.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<StudentDTO>> typeReference = new TypeReference<List<StudentDTO>>() {
		};
		List<StudentDTO> studentResult = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(studentResult.get(0).getId()).contains(studentId.toString());
	}

	@Test
	public void getStudentByIdSchoolIsError() throws JsonProcessingException, Exception {
		idSchool = 6666;
		doThrow(NullPointerException.class).when(studentService).getBySchool(idSchool.toString(), fullDetail);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/student?fullDetail=false", idSchool.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	@Test
	public void getStudentByIdCourseIsOk() throws JsonProcessingException, Exception {
		StudentDTO studentDTO = StudentDTO.builder().id(studentId.toString()).name("Oscar").build();

		List<StudentDTO> students = new ArrayList<>();
		students.add(studentDTO);

		Mockito.when(studentService.getByCourse(idSchool.toString(), idCourse.toString(), fullDetail))
				.thenReturn(students);

		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/school/{schoolId}/student/course/{courseId}?fullDetail=false",
										idSchool.toString(), idCourse.toString())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<StudentDTO>> typeReference = new TypeReference<List<StudentDTO>>() {
		};
		List<StudentDTO> studentResult = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(studentResult.get(0).getId()).contains(studentId.toString());

	}

	@Test
	public void getStudentByIdCourseIsError() throws JsonProcessingException, Exception {
		idCourse = UUID.randomUUID();
		doThrow(NullPointerException.class).when(studentService).getByCourse(idSchool.toString(), idCourse.toString(),
				fullDetail);

		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/school/{schoolId}/student/course/{courseId}?fullDetail=false",
										idSchool.toString(), idCourse.toString())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

}
