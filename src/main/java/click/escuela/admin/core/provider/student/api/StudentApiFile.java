package click.escuela.admin.core.provider.student.api;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class StudentApiFile extends StudentApi{

	private Integer line;
	
	public StudentApiFile(PersonApi personApi, ParentApi parentApi, String grade,
			String division, String level, Integer line) {
		
		super(personApi, parentApi, grade, division, level);
		this.line = line;
		
	}
}
