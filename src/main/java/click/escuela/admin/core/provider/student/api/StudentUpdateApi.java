package click.escuela.admin.core.provider.student.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Student Api")
@NoArgsConstructor
public class StudentUpdateApi extends StudentApi {

	@Valid
	@NotNull(message = "Id cannot be null")
	@JsonProperty(value = "id", required = true)
	private String id;

	public StudentUpdateApi(StudentApi studenApi) {
		super(studenApi.getName(), studenApi.getSurname(), studenApi.getDocument(), studenApi.getGender(),
				studenApi.getBirthday(), studenApi.getAdressApi(), studenApi.getCellPhone(), studenApi.getEmail(),
				studenApi.getParentApi(), studenApi.getSchool(), studenApi.getGrade(), studenApi.getDivision());
	}
}
