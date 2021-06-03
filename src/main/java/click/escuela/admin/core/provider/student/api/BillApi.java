package click.escuela.admin.core.provider.student.api;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@Schema(description = "Bill Api")
@AllArgsConstructor
@SuperBuilder
public class BillApi {
	@NotNull(message = "Month cannot be empty")
	@Min(value = 1, message = "Month should not be less than 1")
    @Max(value = 12, message = "Month should not be greater than 12")
	@JsonProperty(value = "month", required = true)
	private Integer month;
	
	@NotNull(message = "Year cannot be empty")
	@JsonProperty(value = "year", required = true)
	private Integer year;

	@NotNull(message = "Amount cannot be empty")
	@JsonProperty(value = "amount", required = true)
	private Double amount;
	
	@NotBlank(message = "File cannot be empty")
	@Size(max = 10, message = "File must be 50 characters")
	@JsonProperty(value = "file", required = true)
	private String file;
}
