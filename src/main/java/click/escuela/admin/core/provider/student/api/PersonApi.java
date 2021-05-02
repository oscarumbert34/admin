package click.escuela.admin.core.provider.student.api;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Person Api")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PersonApi {

	@NotBlank(message = "Name cannot be empty")
	@Size(max = 50, message = "Name must be 50 characters")
	@JsonProperty(value = "name", required = true)
	private String name;

	@NotBlank(message = "Surname cannot be empty")
	@Size(max = 50, message = "surname must be 50 characters")
	@JsonProperty(value = "surname", required = true)
	private String surname;

	@NotBlank(message = "Document cannot be empty")
	@Size(min = 7, max = 9, message = "Document must be between 7 and 9 characters")
	@JsonProperty(value = "document", required = true)
	private String document;

	@NotBlank(message = "Gender cannot be null")
	@JsonProperty(value = "gender", required = true)
	private String gender;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty(value = "birthday", required = true)
	private LocalDate birthday;

	@JsonProperty(value = "adress", required = true)
	@Valid
	private AdressApi adressApi;

	@NotBlank(message = "CellPhone cannot be null")
	@JsonProperty(value = "cellPhone", required = true)
	private String cellPhone;

	@JsonProperty(value = "email", required = false)
	private String email;
}
