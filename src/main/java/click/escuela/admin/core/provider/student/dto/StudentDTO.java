package click.escuela.admin.core.provider.student.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StudentDTO {
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "name")
	private String name;
	
	@JsonProperty(value = "surname")
	private String surname;
	
	@JsonProperty(value = "document")
	private String document;
	
	@JsonProperty(value = "gender")
	private String gender;
	
	@JsonProperty(value = "grade")
	private String grade;
	
	@JsonProperty(value = "division")
	private String division;
	
	@JsonProperty(value = "level")
	private String level;
	
	@JsonProperty(value = "birthday")
	private LocalDate birthday;
	
	@JsonProperty(value = "adress")
	private AdressDTO adress;
	
	@JsonProperty(value = "cellPhone")
	private String cellPhone;
	
	@JsonProperty(value = "email")
	private String email;
	
	@JsonProperty(value = "parent")
	private ParentDTO parent;
}