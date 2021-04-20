package click.escuela.admin.core.provider.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {

	@JsonProperty(value = "gradeId")
	private String gradeId;
	
	@JsonProperty(value = "name")
	private String name;
	
	@JsonProperty(value = "year")
	private Integer year;
	
	@JsonProperty(value = "division")
	private String division;
	
	@JsonProperty(value = "countStudent")
	private Integer countStudent;

	@JsonProperty(value = "teacher")
	private String teacher;;
}
