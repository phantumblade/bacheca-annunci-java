package eccezioni;

//L'interfaccia definisce i metodi che un gestore di bacheca deve supportare.

public class GestoreBachecaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GestoreBachecaException(String message) {
		super(message);
	}
}
