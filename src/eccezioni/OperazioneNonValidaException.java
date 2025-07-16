package eccezioni;

public class OperazioneNonValidaException extends BachecaException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperazioneNonValidaException(String message) {
        super(message);
    }
}
