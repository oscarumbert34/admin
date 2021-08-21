package click.escuela.admin.core.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.ExcelApi;
import click.escuela.admin.core.provider.student.service.impl.ExcelServiceImpl;
import click.escuela.admin.core.rest.ExcelController;
import click.escuela.admin.core.rest.handler.Handler;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class ExcelControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private ExcelController excelController;

	@Mock
	private ExcelServiceImpl excelService;

	private ObjectMapper mapper;
	private ExcelApi excelApi;
	private Integer idSchool;
	private File file = new File("EstudiantesTest.xlsx");
	private MockMultipartFile multipart ;
	
	@Before
	public void setup() throws TransactionException, Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(excelController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		idSchool = 1234;
		excelApi =ExcelApi.builder().name(file.getName()).schoolId(Integer.valueOf("1234")).file(file.getAbsolutePath()).studentCount(1).build();
		InputStream fileInp = new FileInputStream(file);
		multipart = new MockMultipartFile("EstudiantesTest.xlsx", file.getName(), "application/vnd.ms-excel",fileInp);
	
		//doNothing().when(excelService).save(Mockito.anyString(), Mockito.any());
		ReflectionTestUtils.setField(excelController, "excelService", excelService);
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString()).content(multipart.getBytes())))
				.contains("");
	}
	

	@Test
	public void whenCreateErrorNameEmpty() throws JsonProcessingException, Exception {
		excelApi.setName(StringUtils.EMPTY);
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString()).content(multipart.getBytes())))
				.contains("");
	}

	@Test
	public void whenCreateErrorSchoolNull() throws JsonProcessingException, Exception {
		excelApi.setSchoolId(null);
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString()).content(multipart.getBytes())))
				.contains("");
	}

	@Test
	public void whenCreateErrorFileEMpty() throws JsonProcessingException, Exception {
		excelApi.setFile(StringUtils.EMPTY);
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString()).content(multipart.getBytes())))
				.contains("");
	}

	@Test
	public void whenCreateErrorStudentCountNull() throws JsonProcessingException, Exception {
		excelApi.setStudentCount(null);
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString()).content(multipart.getBytes())))
				.contains("");
	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {
		/*doThrow(new ExcelException(ExcelMessage.CREATE_ERROR)).when(excelService).save(Mockito.anyString(),
				Mockito.any());*/
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString()).content(multipart.getBytes())))
				.contains("");
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private String resultExcelApi(MockHttpServletRequestBuilder requestBuilder)
			throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content(toJson(excelApi)))
				.andReturn().getResponse().getContentAsString();
	}
	
	/* File f = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Penguins.jpg");
    System.out.println(f.isFile()+"  "+f.getName()+f.exists());
    FileInputStream fi1 = new FileInputStream(f);
    FileInputStream fi2 = new FileInputStream(new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Tulips.jpg"));
    MockMultipartFile fstmp = new MockMultipartFile("upload", f.getName(), "multipart/form-data",fi1);
    MockMultipartFile secmp = new MockMultipartFile("upload", "Tulips.jpg","multipart/form-data",fi2); 
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/AddContacts")                
            .file(fstmp)
            .file(secmp)
            .param("name","abc").param("email","abc@gmail.com").param("phone", "1234567890"))               
            .andExpect(status().isOk());*/
}
