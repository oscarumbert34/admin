package click.escuela.admin.core.provider.student.api;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Parent Api")
@NoArgsConstructor
@SuperBuilder
public class ParentApi extends PersonApi{

	public ParentApi(String name, String surname, String document, String gender, LocalDate birthday,
			AdressApi adressApi, String cellPhone, String email) {

		this.setName(name);
		this.setSurname(surname);
		this.setDocument(document);
		this.setGender(gender);
		this.setBirthday(birthday);
		this.setAdressApi(adressApi);
		this.setCellPhone(cellPhone);
		this.setEmail(email);
	}


}
