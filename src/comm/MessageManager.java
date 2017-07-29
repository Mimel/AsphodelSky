package comm;

import display.FooterComponent;

/**
 * Manages messages received from a given stream to implement in the game.
 * @author Matt Imel
 *
 */
public class MessageManager implements Runnable {
	
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
	private String[] messageBuffer;
	
	/**
	 * Lock, ensures no busywaiting for the MM thread.
	 */
	private final Object lock = new Object();
	
	/**
	 * The output.
	 */
	private FooterComponent messageOutput;
	
	public MessageManager(FooterComponent fc) {
		this.messageOutput = fc;
		
		this.messageBuffer = new String[MESSAGEMAX];
		this.messageHead = 0;
	}

	/**
	 * Inserts a message into the buffer. If the buffer is full, the
	 * least recent message is overridden.
	 * @param msg The message to insert.
	 */
	public void insertMessage(String msg) {
		synchronized(lock) {
			if(messageHead < MESSAGEMAX) {
				messageBuffer[messageHead++] = msg;
			} else {
				shiftBuffer(msg);
			}
			
			//Update the GUI.
			lock.notify();
		}
		
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
	
	/**
	 * Wipes the buffer, replacing all entries with NULL.
	 */
	public synchronized void clearBuffer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void run() {
		while(true) {
			synchronized(lock) {
				try {
					lock.wait();
				} catch(Exception e) {
					
				}
				messageOutput.updateMessages(messageBuffer);
			}
		}
	}
	
	public void loadSourceDescPair(String src, String desc) {
		messageOutput.insertItem(src, desc);
	}
}
