package click.escuela.admin.core.exception;

public class TransactionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5985148238792633272L;
	private final String code;
	
	public TransactionException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
