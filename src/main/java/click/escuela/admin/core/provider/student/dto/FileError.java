package click.escuela.admin.core.provider.student.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileError {

	private Integer line;
	private List<String> errors;
}
