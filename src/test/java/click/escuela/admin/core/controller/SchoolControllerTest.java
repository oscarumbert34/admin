package click.escuela.admin.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.enumator.CourseMessage;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.SchoolApi;
import click.escuela.admin.core.rest.SchoolController;
import click.escuela.admin.core.rest.handler.Handler;
import click.escuela.admin.core.service.impl.SchoolServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class SchoolControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private SchoolController schoolController;

	@Mock
	private SchoolServiceImpl schoolService;

	private ObjectMapper mapper;
	private SchoolApi schoolApi;

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(schoolController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(schoolController, "schoolService", schoolService);

		schoolApi = SchoolApi.builder().name("Colegio Nacional").cellPhone("1534567890").email("nacio@edu.com.ar")
				.adress("Zuviria 2412").countCourses(23).countStudent(120).build();

		doNothing().when(schoolService).create(Mockito.any());
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.CREATE_OK.name());

	}

	@Test
	public void whenNameEmpty() throws JsonProcessingException, Exception {

		schoolApi.setName(null);
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");

	}

	@Test
	public void whenCellPhoneEmpty() throws JsonProcessingException, Exception {

		schoolApi.setCellPhone(null);
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CellPhone cannot be null");

	}

	@Test
	public void whenCountStudentEmpty() throws JsonProcessingException, Exception {

		schoolApi.setCountStudent(null);
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CountStudent cannot be null");

	}

	@Test
	public void whenCountCourseEmpty() throws JsonProcessingException, Exception {

		schoolApi.setCountCourses(null);
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CountCourses cannot be null");

	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {

		doThrow(new IllegalArgumentException()).when(schoolService).create(Mockito.any());

		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");

	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}
