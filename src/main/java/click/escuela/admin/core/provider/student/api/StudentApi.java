package click.escuela.admin.core.provider.student.api;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Student Api")
public class StudentApi extends PersonApi{

	@JsonProperty(value = "parent", required = true)
	@Valid
	private ParentApi parentApi;

	@NotBlank(message = "School cannot be null")
	@JsonProperty(value = "school", required = true)
	private String school;
	
	@NotBlank(message = "Grade cannot be null")
	@JsonProperty(value = "grade", required = true)
	private String grade;
	
	@NotBlank(message = "Division cannot be null")
	@JsonProperty(value = "division", required = true)
	private String division;

}
