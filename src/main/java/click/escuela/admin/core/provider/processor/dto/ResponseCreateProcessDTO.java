package click.escuela.admin.core.provider.processor.dto;

import java.util.List;

import click.escuela.admin.core.provider.student.api.StudentApiFile;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResponseCreateProcessDTO {

	List<StudentApiFile> students;
	String processId;
}
