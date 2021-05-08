package click.escuela.admin.core.provider.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchoolDTO {
	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "cellPhone")
	private String cellPhone;
	
	@JsonProperty(value = "email")
	private String email;
	
	@JsonProperty(value = "adress")
	private String adress;
	
	@JsonProperty(value = "countStudent")
	private Integer countStudent;
	
	@JsonProperty(value = "countCourses")
	private Integer countCourses;
}
