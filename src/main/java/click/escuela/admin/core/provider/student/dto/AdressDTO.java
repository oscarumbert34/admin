package click.escuela.admin.core.provider.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdressDTO {

	@JsonProperty(value = "street")
	private String street;

	@JsonProperty(value = "number")
	private Integer number;
	
	@JsonProperty(value = "locality")
	private String locality;
}
