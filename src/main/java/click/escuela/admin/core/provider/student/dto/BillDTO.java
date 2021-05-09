package click.escuela.admin.core.provider.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.admin.core.enumator.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BillDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "period")
	private Integer period;
	
	@JsonProperty(value = "amount")
	private Double amount;

	@JsonProperty(value = "file")
	private String file;
	
	@JsonProperty(value = "status")
	private PaymentStatus status;
}
