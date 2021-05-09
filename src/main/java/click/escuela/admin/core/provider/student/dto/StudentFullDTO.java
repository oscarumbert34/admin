package click.escuela.admin.core.provider.student.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentFullDTO extends StudentDTO{

	StudentFullDTO(String id, String name, String surname, String document, String gender, String grade,
			String division, String level, LocalDate birthday, AdressDTO adress, String cellPhone, String email,
			ParentDTO parent,List<BillDTO> bills) {
		super(id, name, surname, document, gender, grade, division, level, birthday, adress, cellPhone, email, parent);
		
		this.bills=bills;
	}

	@JsonProperty(value = "bill")
	private List<BillDTO> bills;
}
