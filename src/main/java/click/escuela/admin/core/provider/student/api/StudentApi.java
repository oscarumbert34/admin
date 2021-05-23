package click.escuela.admin.core.provider.student.api;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Student Api")
@AllArgsConstructor
@SuperBuilder
public class StudentApi extends PersonApi {

	public StudentApi(String name, String surname, String document, String gender, LocalDate birthday,
			AdressApi adressApi, String cellPhone, String email, ParentApi parentApi, Integer schoolId, String grade,
			String division, String level) {

		super(name, surname, document, gender, birthday, adressApi, cellPhone, email);
		this.parentApi = parentApi;
		this.schoolId = schoolId;
		this.grade = grade;
		this.division = division;
		this.level = level;
	}

	@JsonProperty(value = "id", required = false)
	private String id;

	@JsonProperty(value = "parent", required = true)
	@Valid
	private ParentApi parentApi;

	@NotNull(message = "School cannot be null")
	@JsonProperty(value = "schoolId", required = true)
	private Integer schoolId;

	@NotBlank(message = "Grade cannot be null")
	@JsonProperty(value = "grade", required = true)
	private String grade;

	@NotBlank(message = "Division cannot be null")
	@JsonProperty(value = "division", required = true)
	private String division;

	@ApiModelProperty(dataType = "enum", example ="PREESCOLAR, PRIMARIO, SECUNDARIO, TERCIARIO")
	@NotBlank(message = "Level cannot be null")
	@JsonProperty(value = "level", required = true)
	private String level;
	
	@JsonProperty(value = "courseApi", required = false)
	private CourseApi courseApi;

}
