package click.escuela.admin.core.enumator;

public enum BillEnum {

	PAYMENT_STATUS_CHANGED("PAYMENT_DONE", "Se cambio de estado correctamente");

	private String code;
	private String description;

	BillEnum(String code, String description) {
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
