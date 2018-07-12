package display.mainmenu;

import display.music.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The main view (that also contains the controller) for the Main Menu framework.
 */
class MainMenuDisplay extends JPanel {

    private final int OPTION_FONT_SIZE = 36;
    private final int OPTION_BLOCK_STARTING_X = 150;
    private final int OPTION_BLOCK_ENDING_X = 450;
    private final int OPTION_BLOCK_STARTING_Y_DISPLACEMENT = 300;

    /**
     *The model for the Main Menu framework.
     */
    private final MainMenuLogic mml;

    MainMenuDisplay(MainMenuLogic mml, WindowController wc, AudioPlayer ap) {
        setPreferredSize(new Dimension(1200, 900));
        this.mml = mml;

        Action moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionUp();
                ap.playSFX("OptionSelect.mp3");
                repaint();
            }
        };

        Action moveDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionDown();
                ap.playSFX("OptionSelect.mp3");
                repaint();
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
                if(mml.moveSelectedOptionTo(getMousedOverOption(e.getX(), e.getY()))) {
                    ap.playSFX("OptionSelect.mp3");
                    repaint();
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if(getMousedOverOption(e.getX(), e.getY()) != -1) {
                    wc.addViewToTop(mml.getSelectedView());
                }
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

            g2.drawString(mml.getOptionNameAtPosition(i), OPTION_BLOCK_STARTING_X, (this.getHeight() - OPTION_BLOCK_STARTING_Y_DISPLACEMENT) + (i * OPTION_FONT_SIZE));
        }
    }

    private int getMousedOverOption(int x, int y) {
        if (x > OPTION_BLOCK_STARTING_X && x < OPTION_BLOCK_ENDING_X) {
            int startingY = (getHeight() - OPTION_BLOCK_STARTING_Y_DISPLACEMENT - OPTION_FONT_SIZE);
            int endingY = getHeight() - OPTION_BLOCK_STARTING_Y_DISPLACEMENT + ((mml.getNumberOfOptions() - 1) * OPTION_FONT_SIZE);
            if (y > startingY && y < endingY) {
                return (y - startingY) / OPTION_FONT_SIZE;
            }
        }

        return -1;
    }
}
