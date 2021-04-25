package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.enumator.GenderType;
import click.escuela.admin.core.enumator.StudentEnum;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import click.escuela.admin.core.provider.student.api.StudentUpdateApi;
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
	
	private ObjectMapper mapper;
	private StudentApi studentApi;
	private StudentUpdateApi studentUpdateApi;
	private ParentApi parentApi;
	private AdressApi adressApi;
	private static String EMPTY = "";

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(studentController, "studentService", studentService);
		
		adressApi = new AdressApi("Calle falsa","6458","Nogues");
		parentApi = ParentApi.builder().adressApi(adressApi).
				birthday(LocalDate.now()).cellPhone("3534543").document("33543534").
				email("oscar.umnbetrqgmail.com").gender("F").name("oscar").surname("umbert").build();
		
		
		studentApi = StudentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).document("32333222")
				.cellPhone("4534543").division("C").grade("3°").email("oscar@gmail.com").
				gender(GenderType.MALE.toString()).name("oscar").surname("umbert").parentApi(parentApi).school("1234").build();
		
		studentUpdateApi = new StudentUpdateApi(studentApi);
		studentUpdateApi.setId(UUID.randomUUID().toString());
		
		doNothing().when(studentService).create(Mockito.any());
		//doNothing().when(studentService).update(Mockito.any());
		

	}
	
	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.CREATE_OK.name());
		
	}
	@Test
	public void whenCreateNameEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setName(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");
		
	}
	
	@Test
	public void whenUpdatOk() throws JsonProcessingException, Exception {
		
		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi))).andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.UPDATE_OK.name());
		
	}
	
	@Test
	public void whenUpdateErrorService() throws JsonProcessingException, Exception {
		
		doThrow( new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
				StudentEnum.UPDATE_ERROR.getDescription())).when(studentService).update(Mockito.any());
		
		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.UPDATE_ERROR.getDescription());
		
	}
	
	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
	
}
