package duckhunt.GUI;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import duckhunt.controller.GameListener;
import duckhunt.utility.Resources.Resources;

/**
 * @author Vittorio Polverino
 */
public class MainFrame extends JFrame implements GameListener {

    private static final Dimension FRAME_SIZE = new Dimension(800, 600);
    private static final String PRESS_ENTER_TO_GO_BACK = "pressEnter";

    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private InstructionsPanel instructionsPanel;
    private GoBackToMenu goBackAction;

    public MainFrame() {
        initFrame();
    }

    private void initFrame() {
        this.setTitle("Duck Hunt");
        this.setIconImage(Resources.getImage("/images/duckicon.png"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setSizeWithoutInsets(FRAME_SIZE);
        this.setLocationRelativeTo(null);

        menuPanel = new MenuPanel();
        initPanel(menuPanel, true);

        instructionsPanel = new InstructionsPanel();
        initPanel(instructionsPanel, false);

        goBackAction = new GoBackToMenu();

        menuPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point hitPoint = e.getPoint();
                Resources.getSound("/sounds/shoot.wav").play();
                if (hitPoint.x > 397 && hitPoint.x < 499 && hitPoint.y > 95 && hitPoint.y < 124) {
                    gamePanel = new GamePanel();
                    initPanel(gamePanel, false);
                    gamePanel.addListener(MainFrame.this);
                    goBackAction.setPanel(gamePanel);
                    goBackAction.setEnabled(false);
                    gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), PRESS_ENTER_TO_GO_BACK);
                    gamePanel.getActionMap().put(PRESS_ENTER_TO_GO_BACK, goBackAction);
                    swapPanel(menuPanel, gamePanel);
                } else if (hitPoint.x > 312 && hitPoint.x < 585 && hitPoint.y > 149 && hitPoint.y < 181) {
                    goBackAction.setPanel(instructionsPanel);
                    instructionsPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), PRESS_ENTER_TO_GO_BACK);
                    instructionsPanel.getActionMap().put(PRESS_ENTER_TO_GO_BACK, goBackAction);
                    swapPanel(menuPanel, instructionsPanel);
                } else if (hitPoint.x > 397 && hitPoint.x < 499 && hitPoint.y > 204 && hitPoint.y < 236) {
                    System.exit(0);
                }
            }
        });
    }

    private void setSizeWithoutInsets(Dimension pDimension) {
        Insets i = this.getInsets();
        this.setSize(pDimension.width + i.left + i.right, pDimension.height + i.top + i.bottom);
    }

    private void initPanel(JPanel pPanel, boolean pValue) {
        pPanel.setSize(FRAME_SIZE);
        pPanel.setVisible(pValue);
        this.add(pPanel);
    }

    private void swapPanel(JPanel pFrom, JPanel pTo) {
        pTo.setVisible(true);
        pFrom.setVisible(false);
    }

    @Override
    public void gameIsFinished() {
        goBackAction.setEnabled(true);
    }

    public class GoBackToMenu extends AbstractAction {

        private JPanel panel;

        public GoBackToMenu() {
        }

        public void setPanel(JPanel pPanel) {
            this.panel = pPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (panel != null) {
                if (panel.equals(gamePanel)) {
                    swapPanel(panel, menuPanel);
                    panel.invalidate();
                    panel.removeAll();
                    MainFrame.this.getContentPane().remove(panel);
                    MainFrame.this.invalidate();
                    MainFrame.this.validate();
                } else if (panel.equals(instructionsPanel)) {
                    swapPanel(panel, menuPanel);
                }
            }
        }
    }
}

/*menuPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point hitPoint = e.getPoint();
                System.out.println("x: " + hitPoint.getX() + "y: " + hitPoint.getY());;
            }
        });*/
