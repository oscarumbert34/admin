package click.escuela.admin.core.provider.student.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {

	private String NAME;
	private List<StudentDTO> students;
}
