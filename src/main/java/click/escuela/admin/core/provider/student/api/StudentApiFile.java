package click.escuela.admin.core.provider.student.api;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class StudentApiFile extends StudentApi{

	private Integer line;
	
	public StudentApiFile(PersonApi personApi, ParentApi parentApi, Integer schoolId, String grade,
			String division, String level, Integer line) {
		
		super(personApi, parentApi, schoolId, grade, division, level);
		this.line = line;
		
	}
}
