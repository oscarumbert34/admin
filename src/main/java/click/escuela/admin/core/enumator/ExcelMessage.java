package click.escuela.admin.core.enumator;

public enum ExcelMessage {

	CREATE_OK("CREATED_OK", "Se creó el archivo correctamente"),
	CREATE_ERROR("CREATE_ERROR", "No se pudo crear el archivo correctamente");

	private String code;
	private String description;

	ExcelMessage(String code, String description) {
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
