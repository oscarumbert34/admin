package click.escuela.admin.core.provider.student.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "School Api")
@AllArgsConstructor
@SuperBuilder
public class SchoolApi {
	@NotBlank(message = "Name cannot be empty")
	@Size(max = 50, message = "Name must be 50 characters")
	@JsonProperty(value = "name", required = true)
	private String name;

	@NotBlank(message = "CellPhone cannot be null")
	@JsonProperty(value = "cellPhone", required = true)
	private String cellPhone;

	@JsonProperty(value = "email", required = false)
	private String email;
	
	@JsonProperty(value = "adress", required = false)
	private String adress;
	
	@NotNull(message = "CountStudent cannot be null")
	@JsonProperty(value = "countStudent", required = true)
	private Integer countStudent;
	
	@NotNull(message = "CountCourses cannot be null")
	@JsonProperty(value = "countCourses", required = true)
	private Integer countCourses;
	
}
