package click.escuela.admin.core.enumator;

public enum SchoolMessage {

	CREATE_OK("CREATED_OK","Se creó la escuela correctamente"),
	CREATE_ERROR("CREATE_ERROR","No se pudo crear la escuela correctamente"),
	GET_ERROR("GET_ERROR","No se pudo encontrar la escuela");

	private String code;
	private String description;
	
	SchoolMessage(String code, String description) {
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
