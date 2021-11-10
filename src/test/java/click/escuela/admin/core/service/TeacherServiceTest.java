package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.connector.TeacherConnector;
import click.escuela.admin.core.enumator.CourseMessage;
import click.escuela.admin.core.enumator.DocumentType;
import click.escuela.admin.core.enumator.TeacherMessage;
import click.escuela.admin.core.exception.SchoolException;
import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.provider.processor.service.impl.SecurityServiceImpl;
import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.TeacherApi;
import click.escuela.admin.core.provider.student.api.UserApi;
import click.escuela.admin.core.provider.student.connector.SecurityConnector;
import click.escuela.admin.core.provider.student.dto.AdressDTO;
import click.escuela.admin.core.provider.student.dto.TeacherDTO;
import click.escuela.admin.core.provider.student.service.impl.EmailServiceImpl;
import click.escuela.admin.core.provider.student.service.impl.TeacherServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TeacherServiceTest {

	@Mock
	private TeacherConnector teacherConnector;
	
	@Mock
	private SecurityConnector securityConnector;
	
	@Mock
	private SecurityServiceImpl securityServiceImpl;
	
	@Mock
	private EmailServiceImpl emailServiceImpl;

	private TeacherServiceImpl teacherServiceImpl = new TeacherServiceImpl();
	private TeacherApi teacherApi;
	private UUID teacherId;
	private UUID courseId;
	private Integer schoolId;
	private List<TeacherDTO> teachersDTO;
	private List<String> listUUIDs;
	private TeacherDTO teacherDTO;
	private UserApi userApi;

	@Before
	public void setUp() throws TransactionException, SchoolException {

		teacherId = UUID.randomUUID();
		schoolId = 1234;
		courseId = UUID.randomUUID();

		teacherApi = TeacherApi.builder().name("Mariana").surname("Lopez").birthday(LocalDate.now()).documentType("DNI")
				.document("25897863").cellPhone("1589632485").email("mariAna@gmail.com")
				.adressApi(new AdressApi()).build();

		teacherDTO = TeacherDTO.builder().id(teacherId.toString()).name("Mariana").surname("Lopez")
				.birthday(LocalDate.now()).documentType(DocumentType.DNI).document("25897863").cellPhone("1589632485")
				.email("mariAna@gmail.com").adress(new AdressDTO()).build();
		teachersDTO = new ArrayList<>();
		teachersDTO.add(teacherDTO);
		listUUIDs =  new ArrayList<>();
		listUUIDs.add(String.valueOf(teacherId));
		userApi = UserApi.builder().email("mariAna@gmail.com").name("Mariana").password("Mariana2020").surname("Lopez").schoolId(schoolId.toString()).role("TEACHER").build();

		Mockito.when(teacherConnector.getById(schoolId.toString(), teacherId.toString())).thenReturn(teacherDTO);
		Mockito.when(teacherConnector.getByCourseId(schoolId.toString(), courseId.toString())).thenReturn(teachersDTO);
		Mockito.when(teacherConnector.getBySchoolId(schoolId.toString())).thenReturn(teachersDTO);

		Mockito.when(teacherConnector.create(Mockito.any(), Mockito.any())).thenReturn(teacherDTO);
		Mockito.when(securityServiceImpl.saveUser(Mockito.any())).thenReturn(userApi);


		ReflectionTestUtils.setField(teacherServiceImpl, "teacherConnector", teacherConnector);
		ReflectionTestUtils.setField(teacherServiceImpl, "securityServiceImpl", securityServiceImpl);
		ReflectionTestUtils.setField(teacherServiceImpl, "emailServiceImpl", emailServiceImpl);
		ReflectionTestUtils.setField(securityServiceImpl, "securityConnector", securityConnector);

	}
	
	@Test
	public void whenCreateIsOk() throws TransactionException, SchoolException {
		teacherServiceImpl.create(schoolId.toString(), teacherApi);
		verify(teacherConnector).create(schoolId.toString(), teacherApi);
	}
	
	@Test
	public void whenCreateIsOkButUserStudentApiNull() throws TransactionException, SchoolException {
		Mockito.when(securityServiceImpl.saveUser(Mockito.any())).thenReturn(null);
		teacherServiceImpl.create(schoolId.toString(), teacherApi);
		verify(teacherConnector).create(schoolId.toString(), teacherApi);
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		doThrow(new TransactionException(TeacherMessage.CREATE_ERROR.getCode(),
				TeacherMessage.CREATE_ERROR.getDescription())).when(teacherConnector).create(Mockito.any(),
						Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			teacherServiceImpl.create(schoolId.toString(), teacherApi);
		}).withMessage(TeacherMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdateIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.update(schoolId.toString(), teacherApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {
		doThrow(new TransactionException(TeacherMessage.UPDATE_ERROR.getCode(),
				TeacherMessage.UPDATE_ERROR.getDescription())).when(teacherConnector).update(Mockito.any(),
						Mockito.any());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			teacherServiceImpl.update(schoolId.toString(), teacherApi);
		}).withMessage(TeacherMessage.UPDATE_ERROR.getDescription());

	}

	@Test
	public void whenGetByIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.getById(schoolId.toString(), teacherId.toString());
		} catch (Exception e) {
			assertThat(hasError).isFalse();
		}
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		teacherId = UUID.randomUUID();
		when(teacherConnector.getById(Mockito.any(), Mockito.any())).thenThrow(new TransactionException(
				TeacherMessage.GET_ERROR.getCode(), TeacherMessage.GET_ERROR.getDescription()));
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			teacherServiceImpl.getById(schoolId.toString(), teacherId.toString());
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetBySchoolIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.getBySchoolId(schoolId.toString());
		} catch (Exception e) {
			assertThat(hasError).isFalse();
		}
	}

	@Test
	public void whenGetBySchoolIsEmpty() {
		boolean hasEmpty = false;
		schoolId = 6666;
		try {
			if (teacherServiceImpl.getBySchoolId(schoolId.toString()).isEmpty())
				;
		} catch (Exception e) {
			assertThat(hasEmpty).isFalse();
		}
	}

	@Test
	public void whenGetByCourseIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.getByCourseId(schoolId.toString(), courseId.toString());
		} catch (Exception e) {
			assertThat(hasError).isFalse();
		}
	}

	@Test
	public void whenGetByCourseIsEmpty() {
		boolean hasEmpty = false;
		courseId = UUID.randomUUID();
		try {
			if (teacherServiceImpl.getByCourseId(schoolId.toString(), courseId.toString()).isEmpty())
				;
		} catch (Exception e) {
			assertThat(hasEmpty).isFalse();
		}
	}
	
	@Test
	public void whenAddCoursesIsOk() throws TransactionException {
		teacherServiceImpl.addCourses(schoolId.toString(), teacherId.toString(), listUUIDs);
		verify(teacherConnector).addCourses(schoolId.toString(), teacherId.toString(), listUUIDs);
	}

	@Test
	public void whenDeleteCoursesIsOk() throws TransactionException {
		teacherServiceImpl.deleteCourses(schoolId.toString(), teacherId.toString(), listUUIDs);
		verify(teacherConnector).deleteCourses(schoolId.toString(), teacherId.toString(), listUUIDs);
	}

	@Test
	public void whenAddCoursesIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(teacherConnector).addCourses(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyList());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			teacherServiceImpl.addCourses(StringUtils.EMPTY, StringUtils.EMPTY, new ArrayList<>());
		}).withMessage(CourseMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenDeleteCoursesIsError() throws TransactionException {
		doThrow(new TransactionException(CourseMessage.UPDATE_ERROR.getCode(),
				CourseMessage.UPDATE_ERROR.getDescription())).when(teacherConnector).deleteCourses(Mockito.anyString(),
						Mockito.anyString(), Mockito.anyList());
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			teacherServiceImpl.deleteCourses(StringUtils.EMPTY, StringUtils.EMPTY, new ArrayList<>());
		}).withMessage(CourseMessage.UPDATE_ERROR.getDescription());
	}

}
