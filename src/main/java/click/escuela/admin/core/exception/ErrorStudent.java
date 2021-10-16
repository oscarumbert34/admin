package click.escuela.admin.core.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorStudent {

	private final int status;
	private final String message;
	private List<ValidationError> validationErrors = new ArrayList<>();

	public ErrorStudent(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public void addFieldError(String field, String message) {
		ValidationError error = new ValidationError(message, field);
		validationErrors.add(error);
	}

	public List<ValidationError> getFieldErrors() {
		return validationErrors;
	}

	@Override
	public String toString() {
		return validationErrors.stream()
				.map(p -> p.toString().concat(","))
				.collect(Collectors.toList())
				.toString();
	}
	
}
