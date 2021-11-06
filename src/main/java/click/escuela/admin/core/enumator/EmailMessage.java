package click.escuela.admin.core.enumator;

public enum EmailMessage {

	SEND_OK("SEND_OK","Se envió el email correctamente"),
	SEND_ERROR("SEND_ERROR","No se pudo enviar el email");
	

	private String code;
	private String description;
	
	EmailMessage(String code, String description) {
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
