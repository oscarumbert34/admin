package click.escuela.admin.core.enumator;

public enum CourseMessage {

	CREATE_OK("CREATED_OK","Se creó el curso correctamente"),
	CREATE_ERROR("CREATE_ERROR","No se pudo crear el curso correctamente"),
	UPDATE_OK("UPDATE_OK","Se modificó el curso correctamente"),
	UPDATE_ERROR("UPDATE_ERROR","No se pudo modificar el curso correctamente"),
	DELETE_OK("DELETE_STUDENT","se eliminó el curso correctamente");

	private String code;
	private String description;
	
	CourseMessage(String code, String description) {
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
