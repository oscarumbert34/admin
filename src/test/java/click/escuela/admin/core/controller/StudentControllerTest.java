package click.escuela.admin.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.enumator.EducationLevels;
import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.enumator.StudentMessage;
import click.escuela.admin.core.enumator.Validation;
import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.exception.StudentException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
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
	private ParentApi parentApi;
	private AdressApi adressApi;
	private UUID studentId;
	private UUID courseId;
	private Integer schoolId;
	private static String EMPTY = "";
	private final static String URL = "/school/{schoolId}/student";

	@Before
	public void setup() throws TransactionException, SchoolException {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(studentController, "studentService", studentService);

		studentId = UUID.randomUUID();
		courseId = UUID.randomUUID();
		schoolId = 1234;
		adressApi = new AdressApi("Calle falsa", "6458", "Nogues");
		parentApi = ParentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).cellPhone("3534543")
				.document("33543534").email("oscar.umnbetrqgmail.com").gender(GenderType.FEMALE.toString())
				.name("oscar").surname("umbert").build();
		studentApi = StudentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).document("32333222")
				.cellPhone("4534543").division("C").grade("3°").email("oscar@gmail.com")
				.level(EducationLevels.SECUNDARIO.toString()).gender(GenderType.MALE.toString()).name("oscar")
				.surname("umbert").parentApi(parentApi).build();

		StudentDTO studentDTO = StudentDTO.builder().id(studentId.toString()).document("32333222").cellPhone("4534543")
				.division("C").grade("3°").email("oscar@gmail.com").level(EducationLevels.SECUNDARIO.toString())
				.gender(GenderType.MALE.toString()).name("oscar").surname("umbert").build();
		List<StudentDTO> studentsDTO = new ArrayList<>();
		studentsDTO.add(studentDTO);

		Mockito.when(studentService.getBySchool(schoolId.toString(), false)).thenReturn(studentsDTO);
		doNothing().when(studentService).create(Mockito.anyString(), Mockito.any());
		Mockito.when(studentService.getByCourse(schoolId.toString(), courseId.toString(), false))
				.thenReturn(studentsDTO);
		Mockito.when(studentService.getById(schoolId.toString(), studentId.toString(), false)).thenReturn(studentDTO);

	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		assertThat(resultStudentApi(post(URL, schoolId))).contains(StudentMessage.CREATE_OK.name());
	}

	@Test
	public void whenCreateNameEmpty() throws JsonProcessingException, Exception {
		studentApi.setName(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.NAME_EMPTY.getDescription());
	}

	@Test
	public void whenCreateSurnameEmpty() throws JsonProcessingException, Exception {
		studentApi.setSurname(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.SURNAME_EMPTY.getDescription());
	}

	@Test
	public void whenCreateDocumentEmpty() throws JsonProcessingException, Exception {
		studentApi.setDocument(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.DOCUMENT_EMPTY.getDescription());
	}

	@Test
	public void whenCreateDocumentGreaterCharacters() throws JsonProcessingException, Exception {
		studentApi.setDocument("53454646546456564");
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.DOCUMENT_BAD_SIZE.getDescription());
	}

	@Test
	public void whenCreateDocumentLessCharacters() throws JsonProcessingException, Exception {
		studentApi.setDocument("3453");
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.DOCUMENT_BAD_SIZE.getDescription());
	}

	@Test
	public void whenCreateGenderEmpty() throws JsonProcessingException, Exception {
		studentApi.setGender(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.GENDER_NULL.getDescription());
	}

	@Test
	public void whenCreateCellphoneEmpty() throws JsonProcessingException, Exception {
		studentApi.setCellPhone(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.CELL_PHONE_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressEmpty() throws JsonProcessingException, Exception {
		studentApi.setAdressApi(null);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.ADRESS_NULL.getDescription());
	}

	@Test
	public void whenCreateParentEmpty() throws JsonProcessingException, Exception {
		studentApi.setParentApi(null);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.PARENT_NULL.getDescription());
	}

	@Test
	public void whenCreateGradeEmpty() throws JsonProcessingException, Exception {
		studentApi.setGrade(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.GRADE_NULL.getDescription());
	}

	@Test
	public void whenCreateDivisionEmpty() throws JsonProcessingException, Exception {
		studentApi.setDivision(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.DIVISION_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressNumberEmpty() throws JsonProcessingException, Exception {
		adressApi.setNumber(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.NUMBER_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressStreetEmpty() throws JsonProcessingException, Exception {
		adressApi.setStreet(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.STREET_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressLocalityEmpty() throws JsonProcessingException, Exception {
		adressApi.setLocality(EMPTY);
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.LOCALITY_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressNumberGreaterCharacters() throws JsonProcessingException, Exception {
		adressApi.setNumber("544546546464");
		assertThat(resultStudentApi(post(URL, schoolId))).contains(Validation.NUMBER_BAD_SIZE.getDescription());
	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {
		doThrow(new StudentException(StudentMessage.CREATE_ERROR)).when(studentService).create(Mockito.anyString(),
				Mockito.any());
		assertThat(resultStudentApi(post(URL, schoolId))).contains(StudentMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdatOk() throws JsonProcessingException, Exception {
		studentApi.setId(studentId.toString());
		assertThat(resultStudentApi(put(URL, schoolId))).contains(StudentMessage.UPDATE_OK.name());
	}

	@Test
	public void whenUpdateErrorService() throws JsonProcessingException, Exception {
		doThrow(new StudentException(StudentMessage.UPDATE_ERROR)).when(studentService).update(Mockito.anyString(),
				Mockito.any());
		assertThat(resultStudentApi(put(URL, schoolId))).contains(StudentMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void getStudentByIdIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(
				resultStudentApi(get(URL + "/{idStudent}?fullDetail=false", schoolId, studentId.toString())),
				StudentDTO.class)).hasFieldOrPropertyWithValue("id", studentId.toString());
	}

	@Test
	public void getStudentByIdIsError() throws JsonProcessingException, Exception {
		studentId = UUID.randomUUID();
		doThrow(new StudentException(StudentMessage.GET_ERROR)).when(studentService).getById(schoolId.toString(),studentId.toString(),
				false);
		assertThat(resultStudentApi(get(URL + "/{idStudent}?fullDetail=false", schoolId, studentId.toString())))
				.contains(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void getStudentByIdSchoolIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(resultStudentApi(get(URL + "?fullDetail=false", schoolId.toString())),
				new TypeReference<List<StudentDTO>>() {}).get(0).getId()).contains(studentId.toString());
	}

	@Test
	public void getStudentByIdSchoolIsError() throws JsonProcessingException, Exception {
		schoolId = 6666;
		assertThat(mapper.readValue(resultStudentApi(get(URL + "?fullDetail=false", schoolId.toString())),
				new TypeReference<List<StudentDTO>>() {})).isEmpty();
	}

	@Test
	public void getStudentByIdCourseIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(
				resultStudentApi(
						get(URL + "/course/{courseId}?fullDetail=false", schoolId.toString(), courseId.toString())),
				new TypeReference<List<StudentDTO>>() {}).get(0).getId()).contains(studentId.toString());
	}

	@Test
	public void getStudentByIdCourseIsError() throws JsonProcessingException, Exception {
		courseId = UUID.randomUUID();
		assertThat(mapper.readValue(
				resultStudentApi(
						get(URL + "/course/{courseId}?fullDetail=false", schoolId.toString(), courseId.toString())),
				new TypeReference<List<StudentDTO>>() {})).isEmpty();
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private String resultStudentApi(MockHttpServletRequestBuilder requestBuilder)
			throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi)))
				.andReturn().getResponse().getContentAsString();
	}

}
