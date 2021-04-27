package click.escuela.admin.core.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.PersonApi;
import click.escuela.admin.core.provider.student.api.StudentApi;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Course Api")
@SuperBuilder
public class CourseApi {

	@NotNull(message = "Year cannot be empty")
	@JsonProperty(value = "year", required = true)
	private Integer year;
	
	@NotBlank(message = "Division cannot be empty")
	@Size(max = 10, message = "Division must be 50 characters")
	@JsonProperty(value = "division", required = true)
	private String division;

	@NotNull(message = "CountStudent cannot be empty")
	
	@JsonProperty(value = "countStudent", required = true)
	private Integer countStudent;
	
	@NotNull(message = "School cannot be null")
	@JsonProperty(value = "schoolId", required = true)
	private Integer schoolId;
}
