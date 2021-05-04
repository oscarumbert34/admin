package click.escuela.admin.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import click.escuela.admin.core.exception.TransactionException;
import click.escuela.admin.core.model.School;
import click.escuela.admin.core.provider.student.api.SchoolApi;
import click.escuela.admin.core.repository.SchoolRepository;
import click.escuela.admin.core.service.impl.SchoolServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SchoolServiceTest {

	@Mock
	private SchoolRepository schoolRepository;

	private SchoolServiceImpl schoolServiceImpl = new SchoolServiceImpl();
	private SchoolApi schoolApi;

	@Before
	public void setUp() throws TransactionException {

		School school = School.builder().name("Colegio Nacional").cellPhone("1534567890").email("nacio@edu.com.ar")
				.adress("Zuviria 2412").countCourses(23).countStudent(120).build();
		schoolApi = SchoolApi.builder().name("Colegio Nacional").cellPhone("1534567890").email("nacio@edu.com.ar")
				.adress("Zuviria 2412").countCourses(23).countStudent(120).build();

		Mockito.when(schoolRepository.save(school)).thenReturn(school);
		ReflectionTestUtils.setField(schoolServiceImpl, "schoolRepository", schoolRepository);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			schoolServiceImpl.create(schoolApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() {

		Mockito.when(schoolRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			schoolServiceImpl.create(schoolApi);
		}).withMessage("");

	}

}
