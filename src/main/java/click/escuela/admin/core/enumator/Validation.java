package click.escuela.admin.core.enumator;

public enum Validation {
	NAME_EMPTY("Name cannot be empty"), NAME_BAD_SIZE("Name must be less than 50 characters"),
	SURNAME_EMPTY("Surname cannot be empty"), SURNAME_BAD_SIZE("Surname must be less than 50 characters"),
	SCHOOL_ID_NULL("School Id cannot be null"), FILE_EMPTY("File cannot be empty"),
	DOCUMENT_TYPE_EMPTY("Document type cannot be empty"), DOCUMENT_EMPTY("Document cannot be empty"),
	DOCUMENT_BAD_SIZE("Document must be between 7 and 9 characters"), STUDENT_COUNT_NULL("StudentCount cannot be null"),
	GENDER_NULL("Gender cannot be null"), CELL_PHONE_NULL("CellPhone cannot be null"),
	ADRESS_NULL("Adress cannot be null"), PARENT_NULL("Parent cannot be null"), GRADE_NULL("Grade cannot be null"),
	DIVISION_NULL("Division cannot be null"), NUMBER_NULL("Number cannot be null"),
	STREET_NULL("Street cannot be null"), LOCALITY_NULL("Locality cannot be null"),
	NUMBER_BAD_SIZE("Number must be between 2 and 6 characters");

	private Validation(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	private String description;
}
