package click.escuela.admin.core.enumator;

public enum Validation {

	NAME_EMPTY("Name cannot be empty"), NAME_BAD_SIZE("Name must be less than 50 characters"),
	SCHOOL_ID_NULL("School Id cannot be null"), FILE_EMPTY("File cannot be empty"),
	STUDENT_COUNT_NULL("StudentCount cannot be null");

	private Validation(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	private String description;
}
