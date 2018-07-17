package display.game;

/**
 * The component of the window that displays the text feed, tile data, or speech.
 */
public interface FooterComponent {
	/**
	 * Updates the set of messages that will be displayed.
	 * @param msgs The set of messages to display.
	 */
	void updateMessages(String[] msgs);

	/**
	 * Updates the source/description pair that will be displayed.
	 * @param src The name of the Object to show.
	 * @param desc The description of the Object to show.
	 */
	void insertItem(String src, String desc);
}
