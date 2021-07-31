package click.escuela.admin.core.provider.student.api;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class StudentApiFile extends StudentApi{

	private Integer line;
	
	public StudentApiFile(PersonApi personApi, ParentApi parentApi, String grade,
			String division, String level, Integer line) {
		
		super(personApi, parentApi, grade, division, level);
		this.line = line;
		
	}
}
