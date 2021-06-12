package click.escuela.admin.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.enumator.CourseMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.CourseApi;
import click.escuela.admin.core.provider.student.service.impl.CourseServiceImpl;
import click.escuela.admin.core.rest.CourseController;
import click.escuela.admin.core.rest.handler.Handler;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class CourseControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private CourseController courseController;

	@Mock
	private CourseServiceImpl courseService;

	private ObjectMapper mapper;
	private CourseApi courseApi;
	private String teacherId;
	private String id;
	private String studentId;
	private String schoolId;
	private final static String EMPTY = "";
	private final static String EMPTY_COUNT_STUDENT = "CountStudent cannot be empty";
	private final static String NULL_SCHOOL = "School cannot be null";
	private final static String EMPTY_DIVISION = "Division cannot be empty";
	private final static String EMPTY_YEAR = "Year cannot be empty";
	private final static String URL = "/school/{schoolId}/course";

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(courseController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		id = UUID.randomUUID().toString();
		teacherId = UUID.randomUUID().toString();
		studentId = UUID.randomUUID().toString();
		schoolId = String.valueOf(1234);
		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		doNothing().when(courseService).create(Mockito.any(), Mockito.any());
		ReflectionTestUtils.setField(courseController, "courseService", courseService);
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		String response = resultIsOK(
				post(URL, schoolId).contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse()
						.getContentAsString();
		assertThat(response).contains(CourseMessage.CREATE_OK.name());
	}

	@Test
	public void whenCreateYearEmpty() throws JsonProcessingException, Exception {
		courseApi.setYear(null);
		String response = resultNotOk(
				post(URL, schoolId).contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse()
						.getContentAsString();
		assertThat(response).contains(EMPTY_YEAR);
	}

	@Test
	public void whenCreateDivisonEmpty() throws JsonProcessingException, Exception {
		courseApi.setDivision(EMPTY);
		String response = resultNotOk(
				post(URL, schoolId).contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse()
						.getContentAsString();
		assertThat(response).contains(EMPTY_DIVISION);
	}

	@Test
	public void whenCreateCountStudentEmpty() throws JsonProcessingException, Exception {
		courseApi.setCountStudent(null);
		String response = resultNotOk(
				post(URL, schoolId).contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse()
						.getContentAsString();
		assertThat(response).contains(EMPTY_COUNT_STUDENT);
	}

	@Test
	public void whenCreateSchoolNull() throws JsonProcessingException, Exception {
		courseApi.setSchoolId(null);
		String response = resultNotOk(
				post(URL, schoolId).contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse()
						.getContentAsString();
		assertThat(response).contains(NULL_SCHOOL);
	}

	@Test
	public void whenAddStudentOk() throws JsonProcessingException, Exception {
		String response = resultIsOK(put(URL + "/{idCourse}/student/add/{idStudent}", schoolId, id, studentId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.UPDATE_OK.name());
	}

	@Test
	public void whenAddStudentIdCourseEmpty() throws JsonProcessingException, Exception {
		String response = resultNotFound(put(URL + "/{idCourse}/student/add/{idStudent}", schoolId, EMPTY, studentId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(EMPTY);
	}

	@Test
	public void whenAddStudentIdStudentEmpty() throws JsonProcessingException, Exception {
		String response = resultNotFound(put(URL + "/{idCourse}/student/add/{idStudent}", schoolId, id, EMPTY)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(EMPTY);
	}

	@Test
	public void whenDeleteStudentOk() throws JsonProcessingException, Exception {
		String response = resultIsOK(put(URL + "/{idCourse}/student/del/{idStudent}", schoolId, id, studentId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.UPDATE_OK.name());
	}

	@Test
	public void whenDeleteStudentIdCourseEmpty() throws JsonProcessingException, Exception {
		String response = resultNotFound(put(URL + "/{idCourse}/student/del/{idStudent}", schoolId, EMPTY, studentId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(EMPTY);
	}

	@Test
	public void whenDeleteStudentIdStudentEmpty() throws JsonProcessingException, Exception {
		String response = resultNotFound(put(URL + "/{idCourse}/student/del/{idStudent}", schoolId, id, EMPTY)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(EMPTY);
	}

	@Test
	public void whenAddTeacherIsOk() throws JsonProcessingException, Exception {
		String response = resultIsOK(put(URL + "/{idCourse}/teacher/add/{idTeacher}", schoolId, id, teacherId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.UPDATE_OK.name());
	}

	@Test
	public void whenAddTeacherIsError() throws JsonProcessingException, Exception {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(courseService).addTeacher(schoolId, id, teacherId);
		String response = resultNotOk(put(URL + "/{idCourse}/teacher/add/{idTeacher}", schoolId, id, teacherId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenDeleteTeacherIsOk() throws JsonProcessingException, Exception {
		String response = resultIsOK(put(URL + "/{idCourse}/teacher/del/{idTeacher}", schoolId, id, teacherId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.UPDATE_OK.name());
	}

	@Test
	public void whenDeleteTeacherIsError() throws JsonProcessingException, Exception {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(courseService).deleteTeacher(schoolId, id,
						teacherId);
		String response = resultNotOk(put(URL + "/{idCourse}/teacher/del/{idTeacher}", schoolId, id, teacherId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi))).getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.UPDATE_ERROR.getDescription());
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private MvcResult resultIsOK(RequestBuilder requestBuilder) throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder).andExpect(status().is2xxSuccessful()).andReturn();
	}

	private MvcResult resultNotOk(RequestBuilder requestBuilder) throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
	}

	private MvcResult resultNotFound(RequestBuilder requestBuilder) throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
	}
}
