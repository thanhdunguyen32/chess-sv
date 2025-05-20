package game.util.concurrent;

public interface OrderedEventRunnable extends Runnable {

	public static final byte OP_TYPE_OPEN = 1;

	public static final byte OP_TYPE_CLOSE = 2;

	Object getIdentifyer();

	byte getEventType();

}
