package click.escuela.admin.core.provider.student.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class StudentApiFile extends StudentApi{

	private Integer line;
	
}
