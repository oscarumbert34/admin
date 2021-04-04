package click.escuela.admin.core.api;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseApi {

	private String name;
	private List<String> students;
}
