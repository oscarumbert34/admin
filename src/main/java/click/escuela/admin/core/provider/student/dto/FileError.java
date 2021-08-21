package click.escuela.admin.core.provider.student.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FileError {

	private Integer line;
	private List<String> errors;
}
