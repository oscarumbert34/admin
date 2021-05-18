package click.escuela.admin.core.enumator;

public enum TeacherMessage {

	CREATE_OK("CREATED_OK", "Se creó el profesor correctamente"),
	CREATE_ERROR("CREATE_ERROR", "No se pudo crear el profesor correctamente");

	private String code;
	private String description;

	TeacherMessage(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
