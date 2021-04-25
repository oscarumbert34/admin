package click.escuela.admin.core.provider.student.api;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Adress Api")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdressApi {

	@NotBlank(message = "Street cannot be null")
	@JsonProperty(value = "street", required = true)
	@Valid
	private String street;

	@JsonProperty(value = "number", required = false)
	@Size(min = 2, max = 6, message = "Number must be between 2 and 6 characters")
	@Valid
	private String number;

	@NotBlank(message = "Locality cannot be null")
	@JsonProperty(value = "locality", required = true)
	@Valid
	private String locality;
}
