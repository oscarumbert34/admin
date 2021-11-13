package click.escuela.admin.core.provider.student.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.admin.core.enumator.DocumentType;
import click.escuela.admin.core.enumator.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "surname")
	private String surname;

	@JsonProperty(value = "documentType")
	private DocumentType documentType;
	
	@JsonProperty(value = "genderType")
	private GenderType genderType;

	@JsonProperty(value = "document")
	private String document;

	@JsonProperty(value = "birthday")
	private LocalDate birthday;

	@JsonProperty(value = "adress")
	private AdressDTO adress;

	@JsonProperty(value = "cellPhone")
	private String cellPhone;

	@JsonProperty(value = "email")
	private String email;
	
	@JsonProperty(value = "schoolId")
	private String schoolId;

}