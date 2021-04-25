package click.escuela.admin.core.provider.student.dto;

import java.util.List;

import click.escuela.student.dto.StudentDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {

	private String name;
	private List<StudentDTO> STUDENTS;
}
