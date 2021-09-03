package click.escuela.admin.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import click.escuela.admin.core.enumator.BillMessage;
import click.escuela.admin.core.enumator.CourseMessage;
import click.escuela.admin.core.enumator.PaymentStatus;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.student.api.BillApi;
import click.escuela.admin.core.provider.student.api.BillStatusApi;
import click.escuela.admin.core.provider.student.dto.BillDTO;
import click.escuela.admin.core.provider.student.service.impl.BillServiceImpl;
import click.escuela.admin.core.rest.BillController;
import click.escuela.admin.core.rest.handler.Handler;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class BillControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private BillController billController;

	@Mock
	private BillServiceImpl billService;

	private ObjectMapper mapper;
	private BillApi billApi;
	private static String EMPTY = "";
	private UUID id;
	private String schoolId;
	private String studentId;
	private BillStatusApi billStatus = new BillStatusApi();

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(billController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(billController, "billService", billService);

		schoolId = "1234";
		id = UUID.randomUUID();
		studentId = UUID.randomUUID().toString();
		billApi = BillApi.builder().month(6).year(2021).file("Mayo").amount((double) 12000).build();
		billStatus.setStatus(PaymentStatus.CANCELED.name());

		doNothing().when(billService).create(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", schoolId, studentId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.CREATE_OK.name());

	}

	@Test
	public void whenCreateYearNull() throws JsonProcessingException, Exception {

		billApi.setYear(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", schoolId, studentId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Year cannot be empty");

	}

	@Test
	public void whenCreateMonthLess() throws JsonProcessingException, Exception {

		billApi.setMonth(0);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Month should not be less than 1");

	}

	@Test
	public void whenCreateMonthGreater() throws JsonProcessingException, Exception {

		billApi.setMonth(13);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Month should not be greater than 12");

	}

	@Test
	public void whenCreateMonthNull() throws JsonProcessingException, Exception {

		billApi.setMonth(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Month cannot be empty");

	}

	@Test
	public void whenCreateFileEmpty() throws JsonProcessingException, Exception {

		billApi.setFile(EMPTY);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", schoolId, studentId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("File cannot be empty");

	}

	@Test
	public void whenCreateAmountEmpty() throws JsonProcessingException, Exception {

		billApi.setAmount(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", schoolId, studentId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Amount cannot be empty");

	}

	@Test
	public void whenCreateError() throws JsonProcessingException, Exception {

		doThrow(new TransactionException(BillMessage.CREATE_ERROR.getCode(), BillMessage.CREATE_ERROR.getDescription()))
				.when(billService).create(Mockito.anyString(), Mockito.any(), Mockito.any());

		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", schoolId, studentId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillMessage.CREATE_ERROR.getDescription());

	}

	@Test
	public void getBillByStudentIdIsOk() throws Exception {

		BillDTO billDTO = BillDTO.builder().id(id.toString()).month(6).year(2021).file("Mayo")
				.status(PaymentStatus.PENDING).amount((double) 12000).build();
		List<BillDTO> billsDTO = new ArrayList<>();
		billsDTO.add(billDTO);
		Mockito.when(billService.getByStudentId(schoolId, studentId, PaymentStatus.PENDING.toString(), 6, 2021))
				.thenReturn(billsDTO);

		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/school/{schoolId}/bill/student/{studentId}?month=6&year=2021&status=PENDING",
										schoolId.toString(), studentId.toString())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<BillDTO>> typeReference = new TypeReference<List<BillDTO>>() {
		};
		List<BillDTO> billsResult = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(billsResult.get(0).getId()).contains(id.toString());
	}

	@Test
	public void getBillsByStudentIdIsEmpty() throws Exception {
		schoolId = "6666";
		studentId = UUID.randomUUID().toString();
		doThrow(NullPointerException.class).when(billService).getByStudentId(schoolId.toString(), studentId.toString(),
				PaymentStatus.PENDING.toString(), 6, 2021);

		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/school/{schoolId}/bill/student/{studentId}?month=6&year=2021&status=PENDING",
										schoolId.toString(), studentId.toString())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}
	
	@Test
	public void getUpdatePaymentIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(put("/school/{schoolId}/bill/{billId}", schoolId, id.toString())
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billStatus)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillMessage.PAYMENT_STATUS_CHANGED.name());
	}

	@Test
	public void whenUpdatePaymentIsError() throws JsonProcessingException, Exception {
		doThrow(new TransactionException(BillMessage.GET_ERROR.getCode(),BillMessage.GET_ERROR.getDescription())).when(billService).updatePayment(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		MvcResult result = mockMvc
				.perform(put("/school/{schoolId}/bill/{billId}", schoolId, id.toString())
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billStatus)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillMessage.GET_ERROR.getDescription());
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}
