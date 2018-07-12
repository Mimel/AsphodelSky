package display.mainmenu;

import display.music.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main view (that also contains the controller) for the Main Menu framework.
 */
class MainMenuDisplay extends JPanel {

    private static final int OPTION_FONT_SIZE = 36;
    private static final int OPTION_BLOCK_STARTING_X = 150;
    private static final int OPTION_BLOCK_STARTING_Y_DISPLACEMENT = 300;

    /**
     *The model for the Main Menu framework.
     */
    private final MainMenuLogic mml;

    private final AudioPlayer ap;

    MainMenuDisplay(MainMenuLogic mml, WindowController wc) {
        setPreferredSize(new Dimension(1200, 900));
        this.mml = mml;

        String songName = "music/OptionSelect.mp3";
        ap = new AudioPlayer(songName);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(ap);

        Action moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionUp();
                repaint();
                ap.playSound();
            }
        };

        Action moveDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionDown();
                repaint();
                ap.playSound();
            }
        };

        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wc.addViewToTop(mml.getSelectedView());

            }
        };

        Action goBack = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wc.removeTopView();
            }
        };

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "moveUp");
        this.getActionMap().put("moveUp", moveUp);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        this.getActionMap().put("moveDown", moveDown);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        this.getActionMap().put("confirm", confirm);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "goBack");
        this.getActionMap().put("goBack", goBack);

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.setFont(new Font("Georgia", Font.PLAIN, OPTION_FONT_SIZE));

        for(int i = 0; i < mml.getNumberOfOptions(); i++) {
            if(i == mml.getSelectedOption()) {
                g2.setColor(Color.BLUE);
            } else {
                g2.setColor(Color.BLACK);
            }

            g2.drawString(mml.getOptionNameAtPosition(i), OPTION_BLOCK_STARTING_X, (this.getHeight() - OPTION_BLOCK_STARTING_Y_DISPLACEMENT) + (i * (OPTION_FONT_SIZE + 5)));
        }
    }
}
