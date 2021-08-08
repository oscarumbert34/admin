package click.escuela.admin.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.enumator.DocumentType;
import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.enumator.TeacherMessage;
import click.escuela.admin.core.enumator.Validation;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.dto.AdressDTO;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;
import click.escuela.admin.core.provider.student.service.impl.TeacherServiceImpl;
import click.escuela.admin.core.rest.TeacherController;
import click.escuela.admin.core.rest.handler.Handler;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class TeacherControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private TeacherController teacherController;

	@Mock
	private TeacherServiceImpl teacherService;

	private ObjectMapper mapper;
	private TeacherApi teacherApi;
	private AdressApi adressApi;
	private static String EMPTY = "";
	private String id;
	private String schoolId;
	private String courseId;
	private final static String URL = "/school/{schoolId}/teacher/";
	private List<String> listStringIds = new ArrayList<String>();

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(teacherController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(teacherController, "teacherService", teacherService);

		id = UUID.randomUUID().toString();
		schoolId = "1234";
		courseId = UUID.randomUUID().toString();
		listStringIds.add(courseId);
		adressApi = new AdressApi("Calle falsa", "6458", "Nogues");
		teacherApi = TeacherApi.builder().gender(GenderType.FEMALE.toString()).name("Mariana").surname("Lopez")
				.birthday(LocalDate.now()).documentType("DNI").document("25897863").cellPhone("1589632485")
				.email("mariAna@gmail.com").adressApi(adressApi).build();
		TeacherDTO teacherDTO = TeacherDTO.builder().id(id.toString()).name("Mariana").surname("Lopez")
				.birthday(LocalDate.now()).documentType(DocumentType.DNI).document("25897863").cellPhone("1589632485")
				.email("mariAna@gmail.com").adress(new AdressDTO()).build();
		List<TeacherDTO> teachersDTO = new ArrayList<>();
		teachersDTO.add(teacherDTO);

		doNothing().when(teacherService).create(Mockito.any(), Mockito.any());
		Mockito.when(teacherService.getById(schoolId, id)).thenReturn(teacherDTO);
		Mockito.when(teacherService.getBySchoolId(schoolId)).thenReturn(teachersDTO);
		Mockito.when(teacherService.getByCourseId(schoolId, courseId)).thenReturn(teachersDTO);
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(TeacherMessage.CREATE_OK.name());

	}

	@Test
	public void whenCreateTests() throws JsonProcessingException, Exception {
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi)))).contains(TeacherMessage.CREATE_OK.name());

		doThrow(new TransactionException(TeacherMessage.CREATE_ERROR.getCode(),
				TeacherMessage.CREATE_ERROR.getDescription())).when(teacherService).create(Mockito.any(),
						Mockito.any());
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(TeacherMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenCreateNameEmpty() throws JsonProcessingException, Exception {
		teacherApi.setName(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.NAME_EMPTY.getDescription());
	}

	@Test
	public void whenCreateSurnameEmpty() throws JsonProcessingException, Exception {
		teacherApi.setSurname(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.SURNAME_EMPTY.getDescription());
	}

	@Test
	public void whenCreateDocumentEmpty() throws JsonProcessingException, Exception {
		teacherApi.setDocument(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_EMPTY.getDescription());
	}

	@Test
	public void whenCreateDocumentGreaterCharacters() throws JsonProcessingException, Exception {
		teacherApi.setDocument("53454646546456564");
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_BAD_SIZE.getDescription());
	}

	@Test
	public void whenCreateDocumentLessCharacters() throws JsonProcessingException, Exception {
		teacherApi.setDocument("3453");
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_BAD_SIZE.getDescription());
	}

	@Test
	public void whenCreateDocumentTypeEmpty() throws JsonProcessingException, Exception {
		teacherApi.setDocumentType(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_TYPE_EMPTY.getDescription());
	}

	@Test
	public void whenCreateGenderEmpty() throws JsonProcessingException, Exception {
		teacherApi.setGender(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.GENDER_NULL.getDescription());
	}

	@Test
	public void whenCreateCellphoneEmpty() throws JsonProcessingException, Exception {
		teacherApi.setCellPhone(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.CELL_PHONE_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressEmpty() throws JsonProcessingException, Exception {
		teacherApi.setAdressApi(null);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.ADRESS_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressNumberEmpty() throws JsonProcessingException, Exception {
		adressApi.setNumber(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.NUMBER_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressStreetEmpty() throws JsonProcessingException, Exception {
		adressApi.setStreet(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.STREET_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressLocalityEmpty() throws JsonProcessingException, Exception {
		adressApi.setLocality(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.LOCALITY_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressNumberGreaterCharacters() throws JsonProcessingException, Exception {
		adressApi.setNumber("544546546464");
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.NUMBER_BAD_SIZE.getDescription());
	}

	@Test
	public void whenUpdateTests() throws JsonProcessingException, Exception {
		assertThat(result(put(URL, schoolId, id).content(toJson(teacherApi))))
				.contains(TeacherMessage.UPDATE_OK.name());

		doThrow(new TransactionException(TeacherMessage.UPDATE_ERROR.getCode(),
				TeacherMessage.UPDATE_ERROR.getDescription())).when(teacherService).update(Mockito.anyString(),
						Mockito.any());
		assertThat(result(put(URL, schoolId, id).content(toJson(teacherApi))))
				.contains(TeacherMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void getByIdIsTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL + "{teacherId}", schoolId, id)), TeacherDTO.class))
				.hasFieldOrPropertyWithValue("id", id);

		doThrow(new TransactionException(TeacherMessage.GET_ERROR.getCode(), TeacherMessage.GET_ERROR.getDescription()))
				.when(teacherService).getById(Mockito.anyString(), Mockito.anyString());
		assertThat(result(get(URL + "{teacherId}", schoolId, id))).contains(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void getBySchoolIdTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL, schoolId)), new TypeReference<List<TeacherDTO>>() {
		}).get(0).getId()).contains(id.toString());

		assertThat(mapper.readValue(result(get(URL, "6666")), new TypeReference<List<TeacherDTO>>() {
		})).isEmpty();
	}

	@Test
	public void getByCourseIdTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL + "course/{courseId}", schoolId, courseId)),
				new TypeReference<List<TeacherDTO>>() {
				}).get(0).getId()).contains(id.toString());

		assertThat(mapper.readValue(result(get(URL + "course/{courseId}", schoolId, UUID.randomUUID().toString())),
				new TypeReference<List<TeacherDTO>>() {
				})).isEmpty();
	}

	@Test
	public void whenAddCoursesTests() throws JsonProcessingException, Exception {
		assertThat(result(put(URL + "{idTeacher}/add/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.UPDATE_OK.name());

		doThrow(new TransactionException(TeacherMessage.GET_ERROR.getCode(), TeacherMessage.GET_ERROR.getDescription()))
				.when(teacherService).addCourses(Mockito.anyString(), Mockito.anyString(), Mockito.anyList());
		assertThat(result(put(URL + "{idTeacher}/add/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenDeleteCoursesTests() throws JsonProcessingException, Exception {
		assertThat(result(put(URL + "{idTeacher}/del/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.UPDATE_OK.name());

		doThrow(new TransactionException(TeacherMessage.GET_ERROR.getCode(), TeacherMessage.GET_ERROR.getDescription()))
				.when(teacherService).deleteCourses(Mockito.anyString(), Mockito.anyString(), Mockito.anyList());
		assertThat(result(put(URL + "{idTeacher}/del/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.GET_ERROR.getDescription());

	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private String result(MockHttpServletRequestBuilder requestBuilder) throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getContentAsString();
	}
}
