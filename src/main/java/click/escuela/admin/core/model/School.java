package click.escuela.admin.core.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "school")
@Entity
@Builder
public class School {
	@Id
	@Column(name = "id_school", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "cell_phone", nullable = false)
	private String cellPhone;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "adrees", nullable = false)
	private String adress;

	@Column(name = "count_student", nullable = false)
	private Integer countStudent;

	@Column(name = "count_courses", nullable = false)
	private Integer countCourses;
}
