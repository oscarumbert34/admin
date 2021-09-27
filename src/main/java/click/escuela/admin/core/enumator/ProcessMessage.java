package click.escuela.admin.core.enumator;

public enum ProcessMessage {

	CREATE_OK("CREATED_OK","Se creó el proceso correctamente"),
	CREATE_ERROR("CREATE_ERROR","No se pudo crear el proceso correctamente"),
	GET_ERROR("GET_ERROR","El proceso que se busca no existe");

	private String code;
	private String description;
	
	ProcessMessage(String code, String description) {
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
