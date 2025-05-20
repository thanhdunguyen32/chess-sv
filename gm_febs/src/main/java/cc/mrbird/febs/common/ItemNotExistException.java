package cc.mrbird.febs.common;

public class ItemNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 165538851759027521L;

	public ItemNotExistException() {
		super();
	}

	public ItemNotExistException(int itemId) {
		super(String.valueOf(itemId));
	}

}
