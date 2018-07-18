package comm;

import java.util.Iterator;

/**
 * Manages messages received from a given stream to implement in the game.
 * @author Matt Imel
 *
 */
public class MessageManager {
	
	/**
	 * The maximum number of strings that may be stored on the buffer.
	 */
	private final int MESSAGEMAX = 6;
	
	/**
	 * The location the next message will be inserted.
	 */
	private int messageHead;
	
	/**
	 * The list of messages that are available to write.
	 */
	private final String[] messageBuffer;
	
	public MessageManager() {
		
		this.messageBuffer = new String[MESSAGEMAX];
		this.messageHead = 0;
	}

	/**
	 * Inserts a set of messages into the buffer. If the buffer is full, the
	 * least recent message is overridden.
	 * @param msgs The messages to insert.
	 */
	public void insertMessage(String... msgs) {
		for(String msg : msgs) {
			if(messageHead < MESSAGEMAX) {
				messageBuffer[messageHead++] = msg;
			} else {
				shiftBuffer(msg);
			}
		}
	}

	public String[] getFeedContents() {
		return messageBuffer;
	}
	
	/**
	 * Shifts the contents of the buffer down one position.
	 * The lowest item in the buffer (The back) is discarded, and the empty
	 * space formed near the front is replaced with the message.
	 * @param msg The message to insert.
	 */
	private void shiftBuffer(String msg) {
		System.arraycopy(messageBuffer, 1, messageBuffer, 0, MESSAGEMAX - 1);
		messageBuffer[MESSAGEMAX - 1] = msg;
	}
}
